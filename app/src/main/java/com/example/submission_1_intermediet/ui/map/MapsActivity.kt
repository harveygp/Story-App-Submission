package com.example.submission_1_intermediet.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.submission_1_intermediet.R
import com.example.submission_1_intermediet.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel by viewModels<MapsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsViewModel.getStoriesLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addManyMarker()
    }

    private fun addManyMarker() {
        lifecycleScope.launch(Dispatchers.Main) {
            mapsViewModel.listStories.observe(this@MapsActivity){
                it.forEach {value ->
                    val latLng = LatLng(value.lat!!, value.lon!!)
                    mMap.addMarker(MarkerOptions().position(latLng).title(value.name))!!
                    if(it.lastOrNull()?.id.equals(value.id)) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }
}
