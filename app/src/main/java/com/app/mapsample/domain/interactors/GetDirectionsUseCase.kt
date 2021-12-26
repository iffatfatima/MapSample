package com.app.mapsample.domain.interactors

import com.app.mapsample.domain.base.BaseUseCase
import com.app.mapsample.domain.repository.DirectionsRepository
import javax.inject.Inject

class GetDirectionsUseCase @Inject constructor(val directionsRepository: DirectionsRepository):
    BaseUseCase<Int?> (){


    public override suspend fun run(params: Int?) {
        params?.let { directionsRepository.getData(it) }?.let { resultChannel.send(it) }
    }
}