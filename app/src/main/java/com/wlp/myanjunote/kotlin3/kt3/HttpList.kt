package com.wlp.myanjunote.kotlin3.kt3

import retrofit2.Response
import retrofit2.http.GET

interface HttpList {
    @GET("test/getName")
    suspend fun getNameData(): Response<UserBean>
}