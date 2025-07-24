package ru.vik.trials.coffee.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import ru.vik.trials.coffee.data.model.AuthReq
import ru.vik.trials.coffee.data.model.AuthResp
import ru.vik.trials.coffee.data.model.LocationResp
import ru.vik.trials.coffee.data.model.MenuResp
import ru.vik.trials.coffee.data.model.BearerToken

/** Интерфейс для работы с API. */
interface CoffeeApi {
    /**
     * Авторизация в системе.
     *
     * @param authData Учетные данные.
     * */
    @POST("/auth/login")
    suspend fun signIn(
        @Body
        authData: AuthReq
    ): Response<AuthResp>

    /**
     * Регистрация в системе.
     *
     * @param authData Учетные данные.
     * */
    @POST("/auth/register")
    suspend fun signUp(
        @Body
        authData: AuthReq
    ): Response<AuthResp>

    /**
     * Получает список кофеин.
     *
     * @param token Токен сессии.
     * */
    @GET("/locations")
    suspend fun getLocations(
        @Header("Authorization")
        token: BearerToken
    ): Response<List<LocationResp>>

    /**
     * Получает меню кофейни.
     *
     * @param token Токен сессии.
     * @param id Идентификатор кофейни.
     * */
    @GET("/location/{id}/menu")
    suspend fun getMenu(
        @Header("Authorization")
        token: BearerToken,
        @Query("id")
        shopId: Int
    ): Response<List<MenuResp>>

    /**
     * Загружает картинку.
     *
     * @param url Ссылка на картинку.
     */
    @GET
    suspend fun downloadImage(
        @Url url: String
    ): Response<ResponseBody>
}