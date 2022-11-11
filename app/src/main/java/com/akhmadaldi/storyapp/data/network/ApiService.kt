package com.akhmadaldi.storyapp.data.network

import com.akhmadaldi.storyapp.data.network.auth.LoginResponse
import com.akhmadaldi.storyapp.data.network.auth.RegisterResponse
import com.akhmadaldi.storyapp.data.network.story.FileUploadResponse
import com.akhmadaldi.storyapp.data.network.story.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("v1/register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("v1/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("v1/stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") authHeader: String
    ): Call<FileUploadResponse>

    @GET("v1/stories")
    fun getListStories(
        @Header("Authorization") authHeader: String
    ): Call<StoryListResponse>

}