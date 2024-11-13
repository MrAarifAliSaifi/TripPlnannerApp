package com.example.tripplnannerapp.base.activities.auth

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.tripplnannerapp.base.BaseActivity
import com.example.tripplnannerapp.base.activities.TripActivity
import com.example.tripplnannerapp.base.viewModal.LoginVM
import com.example.tripplnannerapp.databinding.LoginActivityBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.pixplicity.easyprefs.library.Prefs
import java.io.IOException
import java.util.Locale


class LoginActivity : BaseActivity<LoginActivityBinding, LoginVM>() {
    private val viewmodel: LoginVM by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    override fun initializeViewBinding(): LoginActivityBinding {
        return LoginActivityBinding.inflate(layoutInflater)
    }

    override fun initializeViewModel(): LoginVM {
        return viewmodel
    }

    override fun setupUI() {
        checkLocationPermission()
    }

    override fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewmodel.registerUser(email, password)
            } else {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun observeViewModel(){
        viewModel.authStatus.observe(this, Observer { status ->
            if (status) {
                val intent = TripActivity.getStartIntent(this)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            turnOnLocation()
        }
    }

    private fun turnOnLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        val settingsClient = LocationServices.getSettingsClient(this@LoginActivity)
        val task = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            getCurrentLocation()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this@LoginActivity, LOCATION_PERMISSION_REQUEST_CODE
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("TAG", "Error turning on location: ${sendEx.localizedMessage}")
//                    showToast(sendEx.localizedMessage)
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this@LoginActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    getAddressFromLocation(latitude, longitude)
                } ?: run {
                    Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "Unable to get location")
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Failed to get location: ${e.message}")
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressString = address.getAddressLine(0)
                Prefs.putString("Address",addressString)
            }
        } catch (e: IOException) {
            Log.e("TAG", e.localizedMessage)
        }
    }
}
