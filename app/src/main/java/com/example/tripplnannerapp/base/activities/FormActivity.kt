package com.example.tripplnannerapp.base.activities

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tripplnannerapp.base.BaseActivity
import com.example.tripplnannerapp.base.User
import com.example.tripplnannerapp.base.viewModal.FormVM
import com.example.tripplnannerapp.databinding.FormActivityBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.pixplicity.easyprefs.library.Prefs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FormActivity : BaseActivity<FormActivityBinding, FormVM>() {

    private val formViewmodel: FormVM by viewModels()

    companion object {
        const val TAG = "FormActivity"
        fun getStartIntent(context: Context): Intent {
            return Intent(context, FormActivity::class.java).apply {

            }
        }
    }

    override fun initializeViewBinding(): FormActivityBinding {
        return FormActivityBinding.inflate(layoutInflater)
    }

    override fun initializeViewModel(): FormVM {
        return formViewmodel
    }

    override fun setupUI() {
     binding.location.setText(Prefs.getString("Address"))
    }

    override fun setupListeners() {
        binding.apply {
            saveButton.setOnClickListener {
                if (validateFields()) {
                    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val currentTime = dateFormat.format(Date())
                    val userId = Random.nextInt(1, 50)
                    formViewmodel.saveUserData(
                        userId.toString(),
                        nameEditText.text.toString().trim(),
                        Prefs.getString("Address"),
                        durationEditText.text.toString().trim(),
                        currentTime
                    )
                }
            }
        }
    }

    override fun observeViewModel() {
        formViewmodel.uploadStatus.observe(this) { status ->
            when (status) {
                "true" -> showToast("Data saved successfully.")
                "false" -> showToast("Failed to save data.")
            }
        }
    }


    private fun validateFields(): Boolean {
        binding.apply {
            return if (nameEditText.text.toString().isEmpty()) {
                showToast("Please fill name")
                false
            } else if (location.text.toString().isEmpty()) {
                showToast("Please fill location")
                false
            } else if (durationEditText.text.toString().isEmpty()) {
                showToast("Please fill duration")
                false
            } else {
                true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}