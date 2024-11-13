package com.example.tripplnannerapp.base.viewModal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tripplnannerapp.base.User
import com.example.tripplnannerapp.base.dataBase.AppDao
import com.example.tripplnannerapp.base.dataBase.AppDatabase
import com.example.tripplnannerapp.base.dataBase.UserDataClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class TripVM(application: Application) : AndroidViewModel(application) {



    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    fun fetchUserData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { users.add(it) }
                }
                _userList.value = users
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to fetch data: ${error.message}")
            }
        })
    }



}