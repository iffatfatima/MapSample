package com.app.mapsample.domain.interactors

import com.app.mapsample.domain.base.BaseUseCase
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.repository.LocationRepository
import javax.inject.Inject

class GetLastLocation @Inject constructor(private val locationRepository: LocationRepository)
: BaseUseCase<Nothing?>() {
    override suspend fun run(params: Nothing?) {
        resultChannel.send(Result.State.Loading)
        resultChannel.send(locationRepository.getLocation())
        resultChannel.send(Result.State.Loaded)
    }
}