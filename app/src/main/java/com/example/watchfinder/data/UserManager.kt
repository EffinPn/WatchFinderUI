package com.example.watchfinder.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.watchfinder.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton



private val USER_PREFERENCES_KEY = stringPreferencesKey("current_user")
@Singleton
class UserManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private var _currentUser: User? = null

    private val dataStore: DataStore<Preferences> = appContext.dataStore

    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = _userFlow.asStateFlow()


    private val userScope = kotlinx.coroutines.GlobalScope

    private val userObjectKey = stringPreferencesKey("user_object_data")

    init {
        Log.d("UserManager", "Init UserManager (hashCode: ${this.hashCode()})")
        loadUserFromDataStore()
    }


    private fun loadUserFromDataStore() = userScope.launch {
        Log.d("UserManager", "Attempting to load user from DataStore...")
        try {
            dataStore.data
                .map { preferences ->
                    val userJson = preferences[userObjectKey]
                    userJson?.let {
                        Json.decodeFromString<User>(it)
                    }
                }
                .collect { user ->

                    _currentUser = user
                    _userFlow.value = user
                    if (user == null) {
                        Log.d("UserManager", "User loaded from DataStore: null")
                    } else {
                        Log.d("UserManager", "User loaded from DataStore: ${user.username}, Image: ${user.profileImageUrl}")
                    }
                }
        } catch (e: Exception) {

            Log.e("UserManager", "Error loading user from DataStore", e)
            e.printStackTrace()

            _currentUser = null
            _userFlow.value = null
        }
    }



    suspend fun setCurrentUser(user: User?) {
        _currentUser = user
        _userFlow.value = user
        if (user != null) {
            user.profileImageUrl?.let { Log.e("UserManager", it) }
            saveUserToDataStore(user)
        }else{
            clearUserFromDataStore()
        }
    }


    suspend fun clearCurrentUser() {
        _currentUser = null
        _userFlow.value = null
        clearUserFromDataStore()
    }

    suspend fun updateProfileImageUrl(newImageUrl: String?) {
        Log.d("UserManager", "Attempting to update profile image URL to: $newImageUrl")

        val currentUser = _currentUser

        if (currentUser != null) {

            val updatedUser = currentUser.copy(profileImageUrl = newImageUrl)

            setCurrentUser(updatedUser)
            Log.d("UserManager", "Profile image URL updated and setCurrentUser called.")
        } else {

            Log.e("UserManager", "Cannot update profile image URL: current user is null.")
        }
    }


    private suspend fun saveUserToDataStore(user: User) {
        Log.d("UserManager", "Attempting to save user to DataStore: ${user.username}")
        try {
            dataStore.edit { preferences ->

                val userJson = Json.encodeToString(user)

                preferences[userObjectKey] = userJson
            }

            Log.d("UserManager", "User successfully saved to DataStore: ${user.username}")
        } catch (e: Exception) {

            Log.e("UserManager", "Error saving user to DataStore: ${user.username}", e)
            e.printStackTrace()
        }
    }


    private suspend fun clearUserFromDataStore() {
        Log.d("UserManager", "Attempting to clear user from DataStore.")
        try {
            dataStore.edit { preferences ->

                preferences.remove(userObjectKey)
            }
            Log.d("UserManager", "User successfully cleared from DataStore.")
        } catch (e: Exception) {

            Log.e("UserManager", "Error clearing user from DataStore", e)
            e.printStackTrace()
        }
    }

    var resetToken: String? = null
        private set
    fun setResetToken(token: String?) {
        resetToken = token
    }
}

