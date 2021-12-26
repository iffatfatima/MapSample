package com.app.mapsample.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.app.codesamples.network.domain.models.MyLocation
import com.app.mapsample.domain.base.Error
import com.app.mapsample.domain.base.Result
import com.app.mapsample.domain.repository.LocationRepository
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

//add directions provider??
@ExperimentalCoroutinesApi
@Singleton
class LocationRepositoryImpl @Inject constructor(@ApplicationContext val context: Context) : LocationRepository {

    private var locationUpdatesStarted  = false
    private var mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val mLocationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private var mLocationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override suspend fun getLiveLocation(): Flow<Result<MyLocation, Error>> =
        callbackFlow {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationUpdatesStarted = true
                    val loc = locationResult.lastLocation
                    offer(Result.Success(MyLocation(loc.longitude, loc.latitude)))
                }
            }

            if(!locationUpdatesStarted) {
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback as LocationCallback, Looper.getMainLooper()
                )
            }
            awaitClose {
                mLocationCallback?.let { mFusedLocationClient.removeLocationUpdates(it) }
            }
        }

    @SuppressLint("MissingPermission")
    override suspend fun getLocation(): Result<MyLocation, Error> = suspendCancellableCoroutine{
        try {
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        val resultSuccess =
                            Result.Success(MyLocation(task.result.longitude, task.result.latitude))
                        it.resume(resultSuccess)
                    }
                    else -> {
                        it.resume(Result.Failure(Error.GenericError))
                    }
                }
            }
        } catch (error: Exception) {
            Log.e("Error", error.toString())
            it.resume(Result.Failure(Error.GenericError))

        }
    }

    override fun stopLiveLocationUpdates(){
        mLocationCallback?.let { mFusedLocationClient.removeLocationUpdates(it) }
    }

}


