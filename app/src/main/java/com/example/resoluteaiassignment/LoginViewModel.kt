package com.example.resoluteaiassignment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun login() {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                } else {
                    // Login failed, handle the error
                }
            }
    }
}