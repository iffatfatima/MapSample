package com.app.mapsample.domain.repository

import com.app.codesamples.network.domain.models.MyLocation
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.base.Error
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLiveLocation(): Flow<Result<MyLocation, Error>>
    suspend fun getLocation(): Result<MyLocation, Error>
    fun stopLiveLocationUpdates()//should be something like completable

}