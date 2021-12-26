package com.app.codesamples.network.data.repository

import android.util.Log
import com.app.codesamples.network.data.network.DirectionsService
import com.app.codesamples.network.domain.models.MyLocation
import com.app.mapsample.domain.repository.DirectionsRepository
import com.app.mapsample.domain.base.Error
import com.app.mapsample.domain.base.Result
import javax.inject.Inject


class DirectionsRepositoryImpl @Inject constructor(val directionsService: DirectionsService) :
    DirectionsRepository {
    override suspend fun getData(id: Int): Result<MyLocation, Error> {
        try {
//            val response = directionsService.getData(id)
//
//            if (response.isSuccessful && response.body() != null) {
//                val res = (response.body() as DataResponse)
//                return Result.Success(res.toDataItem())
//            } else {
//                Log.e("Error", response.errorBody().toString())
//                return Result.Failure(Error.ResponseError)
//            }
            return Result.Failure(Error.ResponseError)
        } catch (error: Exception) {
            Log.e("Error", error.toString())
            return Result.Failure(Error.NetworkError)
        }
    }

}


