package com.app.codesamples.network.data.network

import com.app.codesamples.network.domain.models.MyLocation
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

//dummy class
interface DirectionsService {
    @POST(Urls.GET_DIRECTIONS)
    suspend fun getDirections(@Query("id") id: Int): Response<MyLocation>
}