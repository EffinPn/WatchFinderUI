package com.example.watchfinder.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.watchfinder.api.ApiService
import com.example.watchfinder.data.UiState.ProfileUiState
import com.example.watchfinder.data.UserManager
import com.example.watchfinder.data.dataStore 
import com.example.watchfinder.data.dto.UserData 
import com.example.watchfinder.data.AppPreferenceKeys 
import com.example.watchfinder.repository.AuthRepository
import com.example.watchfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update 
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.map 

@HiltViewModel
class ProfileVM @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val appContext: Context, 
    private val userManager: UserManager,
    val imageLoader: ImageLoader,
    private val apiService: ApiService, 
    private val authRepository: AuthRepository
) : ViewModel() {

    

    
    private val dataStore: DataStore<Preferences> = appContext.dataStore

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    
    private val _isDarkMode = MutableStateFlow(false) 
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    
    private val _isEditingPass = MutableStateFlow(false)
    val isEditingPass: StateFlow<Boolean> = _isEditingPass.asStateFlow()

    
    private val _showDeleteConfirmation = MutableStateFlow(false)
    val showDeleteConfirmation: StateFlow<Boolean> = _showDeleteConfirmation.asStateFlow()

    
    private val _deleteConfirmationText = MutableStateFlow("")
    val deleteConfirmationText: StateFlow<String> = _deleteConfirmationText.asStateFlow()

    
    var onAccountDeleted: (() -> Unit)? = null

    init {
        
        viewModelScope.launch {
            userManager.userFlow.collect { user ->
                Log.d("ProfileVM", "userFlow collected: ${user?.username}, Image: ${user?.profileImageUrl}")
                _uiState.update { currentState ->
                    val fullUrl = if (!user?.profileImageUrl.isNullOrEmpty()) {
                        user?.profileImageUrl 
                    } else {
                        null
                    }
                    currentState.copy(
                        user = user,
                        newEmail = user?.email ?: "",
                        newUserName = user?.username ?: "",
                        fullProfileImageUrl = fullUrl,
                        isLoading = false
                    )
                }
            }
        }
        
        loadDarkModePreference()

        
        fetchUserProfile()
        fetchProfileImage()
    }

    
    private fun fetchUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getUserProfile()
                userManager.setCurrentUser(user)
                _uiState.update { it.copy(isLoading = false) }
                Log.d("ProfileVM", "Perfil de User cargado correctamente.")
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar perfil: ${e.message ?: "Desconocido"}",
                        user = null 
                    )
                }
                Log.e("ProfileVM", "Error al cargar perfil de usuario", e)
            }
        }
    }

    fun onEmailInputChanged(email: String) {
        _uiState.update { it.copy(newEmail = email, isEmailUpdateSuccess = false, error = null, emailEdited = true) }
    }

    fun onUsernameInputChanged(username: String) {
        _uiState.update { it.copy(newUserName = username, isUserNameUpdateSuccess = false, error = null, usernameEdited = true) }
    }

    fun updateProfileImage(imageUri: Uri?) {
        if (imageUri == null) {
            _uiState.update { it.copy(error = "No se ha seleccionado ninguna imagen") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, isImageUpdateSuccess = false) }

        viewModelScope.launch {
            try {
                val imageBytes = appContext.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    inputStream.readBytes()
                }

                if (imageBytes != null) {
                    val result = userRepository.uploadProfileImage(imageBytes)

                    result.onSuccess { newImageUrl ->
                        Log.d("ProfileVM", "Image upload OK, new URL: $newImageUrl")
                        userManager.updateProfileImageUrl(newImageUrl)
                        _uiState.update { it.copy(isLoading = false, isImageUpdateSuccess = true) } 
                        Log.d("ProfileVM", "carga imagen OK, nueva URL enviada a UserManager: $newImageUrl.")
                    }.onFailure { error ->
                        _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error al subir imagen") }
                        Log.e("ProfileVM", "Error subiendo imagen", error)
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudieron leer los bytes de la imagen") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido al procesar imagen") }
                Log.e("ProfileVM", "General exception processing image", e)
            }
        }
    }

    fun saveProfileChanges() {
        val currentState = _uiState.value 

        
        if (!isEditing.value) { 
            
            
            Log.d("ProfileVM", "No se está editando el perfil (email/username).")
            
            
            return
        }

        val emailToUpdate = if (currentState.emailEdited) currentState.newEmail else null
        val usernameToUpdate = if (currentState.usernameEdited) currentState.newUserName else null

        if (emailToUpdate == null && usernameToUpdate == null) {
            Log.d("ProfileVM", "No hay cambios en email/username para guardar.")
            
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, isEmailUpdateSuccess = false, isUserNameUpdateSuccess = false) }

        viewModelScope.launch {
            try {
                val response = apiService.updateProfile(UserData(email = emailToUpdate, username = usernameToUpdate))

                if (response.isSuccessful) {
                    val updatedUserData = response.body()
                    val newToken = response.headers()["Authorization"]?.substringAfter("Bearer ")

                    if (updatedUserData != null && newToken != null) {
                        Log.d("ProfileVM", "Actualización perfil OK, llamando userManager.setCurrentUser")
                        authRepository.updateToken(newToken)
                        val user = userRepository.getUserProfile() 
                        userManager.setCurrentUser(user)
                        Log.d("ProfileVM", "New token updated via AuthRepository")

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isEmailUpdateSuccess = emailToUpdate != null,
                                isUserNameUpdateSuccess = usernameToUpdate != null,
                                emailEdited = false, 
                                usernameEdited = false
                            )
                        }
                        Log.d("ProfileVM", "Campos (email/username) actualizados con éxito.")
                        
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = "Error: Usuario actualizado pero no se recibió nuevo token.") }
                        Log.e("ProfileVM", "Error: Usuario actualizado pero no se recibió nuevo token.")
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error al actualizar el perfil: ${response.errorBody()?.string() ?: response.message()}") }
                    Log.e("ProfileVM", "Error al actualizar el perfil: ${response.errorBody()?.string() ?: response.message()}")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido al actualizar el perfil") }
                Log.e("ProfileVM", "Error desconocido al actualizar el perfil", e)
            }
            
            
            
        }
    }

    fun setEditing(value: Boolean) {
        _isEditing.value = value
        if (!value) { 
            _uiState.update {
                it.copy(
                    newEmail = it.user?.email ?: "",
                    newUserName = it.user?.username ?: "",
                    emailEdited = false,
                    usernameEdited = false
                )
            }
        }
    }

    fun setPasswordEditing(value: Boolean) {
        _isEditingPass.value = value
        Log.d("ProfileVM", "isEditingPass: ${_isEditingPass.value}")
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccessFlags() {
        _uiState.update {
            it.copy(
                isEmailUpdateSuccess = false,
                isUserNameUpdateSuccess = false,
                isImageUpdateSuccess = false
            )
        }
    }

    private fun loadDarkModePreference() {
        viewModelScope.launch {
            
            
            dataStore.data
                .map { preferences ->
                    preferences[AppPreferenceKeys.DARK_MODE_ENABLED] ?: false 
                }
                .collect { darkMode -> 
                    _isDarkMode.value = darkMode
                }
        }
    }

    
    
    fun setDarkMode(value: Boolean) {
        _isDarkMode.value = value
    }

    
    
    fun saveDarkModePreference(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[AppPreferenceKeys.DARK_MODE_ENABLED] = value
            }
            Log.d("ProfileVM", "Dark mode preference saved: $value")
        }
    }

    fun fetchProfileImage() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val imageUrlResult = userRepository.getProfileImageUrl()
                imageUrlResult.onSuccess { imageUrl ->
                    
                    
                    _uiState.update { it.copy(fullProfileImageUrl = imageUrl, isLoading = false) }
                    Log.d("ProfileVM", "fetchProfileImage: Image URL fetched successfully: $imageUrl")
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Error fetching image URL") }
                    Log.e("ProfileVM", "fetchProfileImage: Error fetching image URL: $error", error)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error fetching image") }
                Log.e("ProfileVM", "fetchProfileImage: General error fetching image", e)
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.changePassword(currentPassword, newPassword)
            if (result.isSuccess) {
                
                _uiState.update { it.copy(isLoading = false, error = "Contraseña cambiada con éxito") }
            } else {
                _uiState.update { it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Error al cambiar la contraseña") }
            }
        }
    }

    fun updateDeleteConfirmationText(text: String) {
        _deleteConfirmationText.value = text
    }

    fun toggleDeleteConfirmation() {
        val oldValue = _showDeleteConfirmation.value
        val newValue = !oldValue
        _showDeleteConfirmation.value = newValue
        Log.d("ProfileVM", "Toggle delete confirmation: $oldValue -> $newValue")
        if (!newValue) { 
            _deleteConfirmationText.value = ""
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            if (_deleteConfirmationText.value.trim() == "DELETE") {
                _uiState.update { it.copy(isLoading = true, error = null) }
                try {
                    userRepository.deleteAccount()
                    authRepository.logout() 
                    _uiState.update { it.copy(isLoading = false) } 
                    onAccountDeleted?.invoke()
                    Log.d("ProfileVM", "onAccountDeleted callback invoked")
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al eliminar la cuenta") }
                } finally {
                    _showDeleteConfirmation.value = false
                    _deleteConfirmationText.value = ""
                }
            } else {
                _uiState.update { it.copy(error = "Texto de confirmación incorrecto") }
            }
        }
    }
}