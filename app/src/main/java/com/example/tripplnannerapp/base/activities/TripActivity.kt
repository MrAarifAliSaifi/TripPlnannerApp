package com.example.tripplnannerapp.base.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripplnannerapp.base.BaseActivity
import com.example.tripplnannerapp.base.UserAdapter
import com.example.tripplnannerapp.base.viewModal.TripVM
import com.example.tripplnannerapp.databinding.TripActivityBinding

class TripActivity : BaseActivity<TripActivityBinding, TripVM>() {

    private val tripViewmodel: TripVM by viewModels()
    private lateinit var userAdapter: UserAdapter


    companion object {
        const val TAG = "TripActivity"
        fun getStartIntent(context: Context): Intent {
            return Intent(context, TripActivity::class.java).apply {

            }
        }
    }

    override fun initializeViewBinding(): TripActivityBinding {
        return TripActivityBinding.inflate(layoutInflater)
    }

    override fun initializeViewModel(): TripVM {
        return tripViewmodel
    }

    override fun setupUI() {
        tripViewmodel.fetchUserData()
    }

    override fun setupListeners() {
        binding.button.setOnClickListener {
            val intent = Intent(FormActivity.getStartIntent(this))
            startActivity(intent)
        }
    }

    override fun observeViewModel() {
        if (isInternetConnected()) {
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            viewModel.userList.observe(this) { users ->
                userAdapter = UserAdapter(users)
                binding.recyclerView.adapter = userAdapter
            }
        }
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            this@TripActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}