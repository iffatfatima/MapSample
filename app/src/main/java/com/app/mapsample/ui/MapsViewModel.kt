package com.app.mapsample.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.codesamples.network.domain.models.MyLocation
import com.app.mapsample.domain.interactors.GetDirectionsUseCase
import com.app.mapsample.domain.interactors.GetLiveLocationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.base.Error
import com.app.mapsample.domain.interactors.GetLastLocation
import com.app.mapsample.domain.interactors.StopLocationUpdatesUseCase
import com.app.mapsample.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getLiveLocationUseCase: GetLiveLocationUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getLastLocation: GetLastLocation,
    private val stopLocationUpdatesUseCase: StopLocationUpdatesUseCase
): ViewModel() {

    private val liveLocationResponse: MutableLiveData<MyLocation> = MutableLiveData()
    val liveLocationLD: LiveData<MyLocation> = liveLocationResponse
    private val lastLocationResponse: MutableLiveData<MyLocation> = MutableLiveData()
    val lastLocationLD: LiveData<MyLocation> = lastLocationResponse

    //start listening for data on the channels defined in use cases
    //consumed in init to make sure it is called once to avoid multiple subscriptions
    init {
        viewModelScope.launch {
            getLastLocation.receiveChannel.consumeEach {
                it.handleResult(::showLastLocationUI, ::showError, ::showState )
            }
            getDirectionsUseCase.receiveChannel.consumeEach {
                it.handleResult(::showDirections, ::showError, ::showState )
            }
        }
    }
    //Usecase defines scope
    fun fetchLiveLocation() {
        viewModelScope.launch {
            getLiveLocationUseCase.run().collect {
                it.handleResult(::updateLiveLocationUI, ::showError, ::showState )
            }
        }
    }
    fun getLastLocation() {
        viewModelScope.launch {getLastLocation.invoke(null)}
    }
    //view model defines scope
    fun getDirections () = viewModelScope.launch {
        getDirectionsUseCase.run(1)
    }

    private fun showState(state: Result.State) {
        Log.d("ViewModel-State", state.toString())

    }

    private fun showError(error: Error) {
        Log.e("ViewModel-Error", error.toString())
    }


    private fun updateLiveLocationUI(myLocation: Any) {
        liveLocationResponse.postValue(myLocation as MyLocation)
    }
    private fun showLastLocationUI(myLocation: Any) {
        lastLocationResponse.postValue(myLocation as MyLocation)
    }
    private fun showDirections(myLocation: Any) {

    }

    override fun onCleared() {
//        stopLocationUpdatesUseCase.invoke(null)//calls clear itself
//        getLiveLocationUseCase.clear()
        stopLocationUpdatesUseCase.invoke(null)
        stopLocationUpdatesUseCase.clear()
        getDirectionsUseCase.clear()
        getLastLocation.clear()
        super.onCleared()
    }
}