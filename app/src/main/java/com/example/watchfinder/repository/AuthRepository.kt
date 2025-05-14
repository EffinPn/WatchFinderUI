package com.example.watchfinder.repository

import com.example.watchfinder.api.ApiService
import com.example.watchfinder.data.UserManager
import com.example.watchfinder.data.dto.ChangePasswordRequest
import com.example.watchfinder.data.dto.LoginRequest
import com.example.watchfinder.data.dto.LoginResponse
import com.example.watchfinder.data.dto.ForgotPasswordRequest
import com.example.watchfinder.data.dto.RegisterRequest
import com.example.watchfinder.data.dto.ResetPasswordRequest
import com.example.watchfinder.data.prefs.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()!!
                tokenManager.saveToken(loginResponse.token)
                println("Exito: ${loginResponse.token}")
                fetchAndStoreUserProfile()
                Result.success(loginResponse)

            } else {
                println("Fallo al logear: ${response.code()}")
                Result.failure(Exception("Fallo al logear: ${response.code()}"))
            }
        } catch (e: Exception) {
            println("Fallo al logear:" + e)
            Result.failure(e)
        }
    }

    suspend fun validate(): Result<Boolean> {
        return try {
            val response = apiService.validate()
            if (response.isSuccessful) {
                fetchAndStoreUserProfile()
                Result.success(true)
            } else {
                tokenManager.clearToken()
                userManager.clearCurrentUser()
                Result.failure(Exception("Validación de token fallida: ${response.code()}"))
            }
        } catch (e: Exception) {
            println("Error de red validando: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun register(
        name: String,
        username: String,
        password: String,
        email: String
    ): Result<String> {
        return try {
            val response = apiService.register(RegisterRequest(name, username, password, email))
            if (response.isSuccessful) {
                val responseString = response.body()?.string() ?: "Respuesta vacía"
                Result.success(responseString)
            } else {
                val errorString = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Fallo al registrar: ${response.code()} - $errorString"))
            }
        } catch (e: Exception) {
            println("Fallo al registrar:" + e)
            Result.failure(e)
        }
    }

    suspend fun fetchAndStoreUserProfile() {
        try {
            val userProfileResponse = apiService.getProfile()
            if (userProfileResponse.isSuccessful) {
                val user = userProfileResponse.body()
                userManager.setCurrentUser(user)
                println("Perfil de usuario obtenido y guardado.")
            } else {
                println("Error al obtener perfil de usuario: ${userProfileResponse.code()}")
                userManager.clearCurrentUser()
            }
        } catch (e: Exception) {
            println("Excepción al obtener perfil de usuario: $e")
            userManager.clearCurrentUser()
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
        userManager.clearCurrentUser()
    }


    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            val response = apiService.sendPasswordResetEmail(ForgotPasswordRequest(email))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Fallo al enviar e-mail: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        return try {
            val response = apiService.resetPassword(ResetPasswordRequest(token, newPassword))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error reseteando contraseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updateToken(newToken: String) {
        tokenManager.saveToken(newToken)
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            val response = apiService.changePassword(ChangePasswordRequest(currentPassword, newPassword))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al cambiar la contraseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

