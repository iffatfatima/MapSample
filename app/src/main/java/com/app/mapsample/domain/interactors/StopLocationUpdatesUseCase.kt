package com.app.mapsample.domain.interactors

import com.app.mapsample.domain.base.BaseUseCase
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.repository.LocationRepository
import javax.inject.Inject

//update to like-completable use case
class StopLocationUpdatesUseCase @Inject constructor(private val locationRepository: LocationRepository)
: BaseUseCase<Nothing?>() {
    override suspend fun run(params: Nothing?) {
        locationRepository.stopLiveLocationUpdates()
    }
}