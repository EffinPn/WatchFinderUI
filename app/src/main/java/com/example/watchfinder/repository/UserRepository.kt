package com.example.watchfinder.repository

import android.util.Log
import com.example.watchfinder.api.ApiService
import com.example.watchfinder.data.Utils
import com.example.watchfinder.data.dto.Item
import com.example.watchfinder.data.dto.ProfileImageUpdateResponse
import com.example.watchfinder.data.dto.UserData
import com.example.watchfinder.data.model.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.IOException
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard
import com.example.watchfinder.data.prefs.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val utils: Utils
){


    suspend fun addToList(id: String, state: String, type: String): Boolean {
        val item = Item(id, state, type)
        return try {
            val response: Response<Void> = apiService.addToList(item)
            if (response.isSuccessful) {
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeFromList(id: String, state: String, type: String): Boolean {
        val item = Item(id, state, type)
        return try {
            val response: Response<Void> = apiService.removeFromList(item)
            if (response.isSuccessful) {
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFavMovies(): List<MovieCard> {
        val apiCards = apiService.getFavMovies()
        return apiCards.map{
                movie -> utils.movieToCard(movie)
        }
    }

    suspend fun getFavSeries(): List<SeriesCard> {
        val apiCards = apiService.getFavSeries()
        return apiCards.map{
                series -> utils.seriesToCard(series)
        }
    }

    suspend fun getSeenMovies(): List<MovieCard> {
        val apiCards = apiService.getSeenMovies()
        return apiCards.map{
                movie -> utils.movieToCard(movie)
        }
    }

    suspend fun getSeenSeries(): List<SeriesCard> {
        val apiCards = apiService.getSeenSeries()
        return apiCards.map{
                series -> utils.seriesToCard(series)
        }
    }

    suspend fun getUserProfile(): User {
        val response: Response<User> = apiService.getProfile()
        if (response.isSuccessful) {

            val user = response.body()
            if (user == null) {
                throw IllegalStateException("user body es null")
            }
            return user
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = "Error recogiendo profile: ${response.code()} ${response.message()} - $errorBody"
            throw IOException(errorMessage)
        }
    }

    suspend fun uploadProfileImage(imageBytes: ByteArray): Result<String> {
        return try {
            val requestFile = imageBytes.toRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", "profile.jpg", requestFile)

            val response: Response<ProfileImageUpdateResponse> = apiService.uploadProfileImage(body)

            if (response.isSuccessful) {
                val imageUrlResponse = response.body()
                val newImageUrl = imageUrlResponse?.profileImageUrl
                if (newImageUrl != null) {
                    Result.success(newImageUrl)
                } else {
                    Result.failure(IOException("Subida con éxito"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(IOException("Error subiendo imagen: ${response.code()} ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getProfileImageUrl(): Result<String> {
        return try {
            val response = apiService.getImageUrl()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.imageUrl.isNotEmpty()) {
                    Log.d("UserRepository", "getProfileImageUrl: Got image URL: ${body.imageUrl}")
                    Result.success(body.imageUrl)
                } else {
                    Result.failure(IOException("No image URL found in response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(IOException("Error fetching image URL: ${response.code()} ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(email: String?, username: String?): Result<User> {
        return try {
            val requestBody = UserData(email = email, username = username)
            val response: Response<UserData> = apiService.updateProfile(requestBody)

            if (response.isSuccessful) {
                val updatedUserData = response.body()
                if (updatedUserData != null) {
                    val user = getUserProfile()
                    Result.success(user)
                } else {
                    Result.failure(IllegalStateException("Actualización conseguida, pero no ha devuelto UserData"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(IOException("Error actualizando perfil: ${response.code()} ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = apiService.deleteAccount()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(IOException("Error al eliminar la cuenta: ${response.code()} ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}


