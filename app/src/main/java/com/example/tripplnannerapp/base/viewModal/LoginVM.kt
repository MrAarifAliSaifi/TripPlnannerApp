package com.example.tripplnannerapp.base.viewModal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginVM : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authStatus = MutableLiveData<Boolean>()
    val authStatus: LiveData<Boolean> get() = _authStatus

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authStatus.value = true
            } else {
                _authStatus.value = false
            }
        }
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authStatus.value = true
            } else {
                _authStatus.value = false
            }
        }
    }
}
