package com.example.tripplnannerapp.base.viewModal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplnannerapp.base.User
import com.example.tripplnannerapp.base.dataBase.AppDao
import com.example.tripplnannerapp.base.dataBase.AppDatabase
import com.example.tripplnannerapp.base.dataBase.UserDataClass
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class FormVM(application: Application)  : AndroidViewModel(application) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val appDao: AppDao = AppDatabase.getDatabase(application).appDao()

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus


    fun saveUserData(
        userId: String, name: String, location: String, duration: String, currentTime: String
    ) {
        val userData = mapOf(
            "name" to name,
            "location" to location,
            "duration" to duration,
            "currentTime" to currentTime,
        )

        database.child(userId).setValue(userData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uploadStatus.value = "true"
                println("Data saved successfully.")
            } else {
                _uploadStatus.value = "false"
                println("Failed to save data: ${task.exception?.message}")
            }
        }
    }

//    fun insertUserToRoom(user: User) {
//        viewModelScope.launch {
//            val userDataClass = UserDataClass(
//                time = user.time,
//                name = user.name,
//                location = user.location,
//                duration = user.duration
//            )
//            appDao.insertItemUrl(userDataClass)
//        }
//    }
}