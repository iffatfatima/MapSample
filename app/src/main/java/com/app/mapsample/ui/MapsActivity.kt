package com.app.mapsample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.mapsample.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.app.mapsample.databinding.ActivityMapsBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.google.android.gms.maps.model.Marker


@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MapsActivity::class.java.simpleName
    }

    private var mMarker: Marker? = null
    private var mMap: GoogleMap?= null
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
                
        viewModel.lastLocationLD.observe(this) { it ->
            Log.d(TAG, "Last Location: ${it.latitude}, ${it.longitude}")
            val lastLatLng = LatLng(it.latitude, it.longitude)
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                mMap = googleMap
                    mMarker = googleMap.addMarker(MarkerOptions().position(lastLatLng).title("I'm moving :D"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng))
                    googleMap.setMinZoomPreference(18f)

            }
        }
        viewModel.liveLocationLD.observe(this, {
            Log.d(TAG, "Updated Location: ${it.latitude}, ${it.longitude}")
            val updatedLatLng = LatLng(it.latitude, it.longitude)
            mMarker?.position = updatedLatLng
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(updatedLatLng))
        })
        viewModel.getLastLocation()
        viewModel.fetchLiveLocation()
    }
}