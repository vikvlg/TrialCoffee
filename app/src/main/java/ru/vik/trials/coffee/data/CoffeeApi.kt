package ru.vik.trials.coffee.data

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.vik.trials.coffee.data.model.AuthReq
import ru.vik.trials.coffee.data.model.AuthResp
import ru.vik.trials.coffee.data.model.LocationResp
import ru.vik.trials.coffee.data.model.Token

interface CoffeeApi {
    @POST("/auth/login")
    fun signIn(
        @Body
        req: AuthReq
    ): Call<AuthResp>

    @POST("/auth/register")
    fun signUp(
        @Body
        req: AuthReq
    ): Call<AuthResp>

    @GET("/locations")
    fun getLocations(
        @Header("Authorization")
        token: Token
    ): Call<List<LocationResp>>

    @GET("/locations")
    suspend fun getLocations2(
        @Header("Authorization")
        token: Token
    ): Response<List<LocationResp>>

    @POST("/auth/login")
    suspend fun signIn2(
        @Body
        req: AuthReq
    ): Response<AuthResp>
}