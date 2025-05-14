package com.example.watchfinder.api

import com.example.watchfinder.data.dto.ChangePasswordRequest
import com.example.watchfinder.data.dto.ForgotPasswordRequest
import com.example.watchfinder.data.dto.ImageUrlResponse
import com.example.watchfinder.data.dto.Item
import com.example.watchfinder.data.dto.LoginRequest
import com.example.watchfinder.data.dto.LoginResponse
import com.example.watchfinder.data.dto.ProfileImageUpdateResponse
import com.example.watchfinder.data.dto.RegisterRequest
import com.example.watchfinder.data.dto.ResetPasswordRequest
import com.example.watchfinder.data.dto.UserData
import com.example.watchfinder.data.model.Movie
import com.example.watchfinder.data.model.Series
import com.example.watchfinder.data.model.User
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @GET("/movies/getall")
    suspend fun getMovies(): List<Movie>
    @GET("/movies/getbygenre")
    suspend fun getMoviesByGenre(@Query("genres") selectedGenres: List<String>): List<Movie>
    @GET("/movies/getbytitle")
    suspend fun getMoviesByTitle(@Query("title") userInput: String): List<Movie>
    @GET("/movies/getbyid")
    suspend fun getMoviesById(@Query("id") itemId: String): Movie

    @GET("/series/getall")
    suspend fun getSeries(): List<Series>
    @GET("/series/getbygenre")
    suspend fun getSeriesByGenre(@Query("genres") selectedGenres: List<String>): List<Series>
    @GET("/series/getbytitle")
    suspend fun getSeriesByTitle(@Query("title") userInput: String): List<Series>
    @GET("/series/getbyid")
    suspend fun getSeriesById(@Query("id") itemId: String): Series

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ResponseBody>
    @GET("/auth/validate")
    suspend fun validate(): Response<Map<String, String>>

    @GET("/users/getprofile")
    suspend fun getProfile(): Response<User>
    @POST("/users/addtolist")
    suspend fun addToList(@Body item: Item): Response<Void>
    @POST("/users/removefromlist")
    suspend fun removeFromList(@Body item: Item): Response<Void>

    @GET("/users/getfavseries")
    suspend fun getFavSeries(): List<Series>
    @GET("/users/getfavmovies")
    suspend fun getFavMovies(): List<Movie>

    @GET("/users/getseenseries")
    suspend fun getSeenSeries(): List<Series>
    @GET("/users/getseenmovies")
    suspend fun getSeenMovies(): List<Movie>
    @GET("/users/recommendmovies")
    suspend fun getMovieRecommendations(): List<Movie>
    @GET("/users/recommendseries")
    suspend fun getSeriesRecommendations(): List<Series>

    @POST("auth/forgot-password")
    suspend fun sendPasswordResetEmail(@Body request: ForgotPasswordRequest): Response<Unit>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    @Multipart
    @PATCH("/users/profile/image")
    suspend fun uploadProfileImage(@Part image: MultipartBody.Part): Response<ProfileImageUpdateResponse>

    @PUT("/users/profile")
    suspend fun updateProfile(@Body updatedUserData: UserData): Response<UserData>

    @GET("users/profile/image-url")
    suspend fun getImageUrl(): Response<ImageUrlResponse>

    @POST("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Unit>

    @DELETE("/users/delete")
    suspend fun deleteAccount(): Response<Unit>
}


