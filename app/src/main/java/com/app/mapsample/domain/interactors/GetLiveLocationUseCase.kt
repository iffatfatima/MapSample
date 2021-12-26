package com.app.mapsample.domain.interactors

import com.app.codesamples.network.domain.models.MyLocation
import com.app.mapsample.domain.base.Error
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLiveLocationUseCase@Inject constructor(private val locationRepository: LocationRepository){
    suspend fun run() : Flow<Result<MyLocation, Error>> {
        return locationRepository.getLiveLocation()
    }
}