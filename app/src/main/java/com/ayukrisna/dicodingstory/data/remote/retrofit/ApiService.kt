package com.ayukrisna.dicodingstory.data.remote.retrofit

import com.ayukrisna.dicodingstory.data.remote.response.DetailStoryResponse
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryResponse
import com.ayukrisna.dicodingstory.data.remote.response.LoginResponse
import com.ayukrisna.dicodingstory.data.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<RegisterResponse>
    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<LoginResponse>
    @GET("stories")
    suspend fun getStories(): Response<ListStoryResponse>
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): Response<DetailStoryResponse>
}