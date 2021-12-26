package com.app.mapsample.domain.repository

import com.app.codesamples.network.domain.models.MyLocation
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.base.Error

interface DirectionsRepository {
    suspend fun getData(page: Int): Result<MyLocation, Error>
}