package com.example.storyapp.ui.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.model.LocationModel
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val args: MapsFragmentArgs by navArgs()
    private val viewModel: MapViewModel by viewModels()

    private val binding: FragmentMapsBinding by lazy {
        FragmentMapsBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    private fun observeAllMapStory() {
        viewModel.allStoryLiveData.observe(requireActivity()) {
            it?.let { it1 ->
                getManyMarker(mMap, it1)
            }
        }

        viewModel.errorResult.observe(requireActivity()){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.loadingLiveData.collect {
                binding.loadingState.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }



    private val boundsBuilder = LatLngBounds.Builder()

    private fun getManyMarker(googleMap: GoogleMap, listStory: List<StoryModel>) {

        val infoWindow = layoutInflater.inflate(R.layout.custom_info_window_map, null)

        listStory.forEach { story ->
            val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            val addressName = getAddressName(story.lat ?: 0.0, story.lon ?: 0.0)

            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet(addressName)
            )

            boundsBuilder.include(latLng)
        }

        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker): View {
                render(marker, listStory, infoWindow)
                return infoWindow
            }

            override fun getInfoWindow(marker: Marker): View {
                render(marker, listStory, infoWindow)
                return infoWindow
            }

        })

        val bounds: LatLngBounds = boundsBuilder.build()
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }

    private fun render(marker: Marker, listStory: List<StoryModel>, view: View) {
        val storyModel = listStory.find { it.name == marker.title }

        Glide.with(binding.root)
            .load(storyModel?.photoUrl)
            .into(view.findViewById(R.id.badge))
        view.findViewById<TextView>(R.id.title).text = storyModel?.name
        view.findViewById<TextView>(R.id.snippet).text = storyModel?.description
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun getMarkerUser() {

        val lat = args.locationModel?.latitude
        val lng = args.locationModel?.longitude

        if (lat != null && lng != null){
            binding.chooseMyLocation.visibility = View.GONE

            val location = LatLng(lat, lng)
            val addressName = getAddressName(lat, lng)
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(args.locationModel?.name)
                    .snippet(addressName)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        } else {
            binding.chooseMyLocation.visibility = View.VISIBLE
            selectMyLocation()
        }

    }

    private fun selectMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title("Posisi Kamu")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))

        actionButtonSelectedButtonMYLocation(location)
    }

    private fun actionButtonSelectedButtonMYLocation(location: Location) {
        binding.chooseMyLocation.setOnClickListener {
            val locationModel = LocationModel("", location.latitude, location.longitude)
            activity?.intent?.putExtra("result", locationModel)
            activity?.setResult(Activity.RESULT_OK, activity?.intent)
            activity?.finish()
        }
    }

    private fun iniViewModel() {
        viewModel.getAllMapStory(1)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (!args.listFeedMap){
            getMarkerUser()
        } else {
            iniViewModel()
            observeAllMapStory()
        }
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        val TAG = MapsFragment::class.java.simpleName
    }
}