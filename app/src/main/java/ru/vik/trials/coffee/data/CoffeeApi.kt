package ru.vik.trials.coffee.data

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.vik.trials.coffee.data.model.AuthReq
import ru.vik.trials.coffee.data.model.AuthResp
import ru.vik.trials.coffee.data.model.LocationResp
import ru.vik.trials.coffee.data.model.MenuResp
import ru.vik.trials.coffee.data.model.Token

interface CoffeeApi {
    @POST("/auth/login")
    suspend fun signIn(
        @Body
        req: AuthReq
    ): Response<AuthResp>

    @POST("/auth/register")
    suspend fun signUp(
        @Body
        req: AuthReq
    ): Response<AuthResp>

    @GET("/locations")
    suspend fun getLocations(
        @Header("Authorization")
        token: Token
    ): Response<List<LocationResp>>

    @GET("/location/{id}/menu")
    suspend fun getMenu(
        @Header("Authorization")
        token: Token,
        @Field("id")
        shopId: Int
    ): Response<List<MenuResp>>
}