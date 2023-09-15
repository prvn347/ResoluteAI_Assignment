package com.example.resoluteaiassignment


import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resoluteaiassignment.ui.theme.ResoluteAIAssignmentTheme
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.resoluteaiassignment.ui.theme.greenColor
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResoluteAIAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                        topBar = {

                            TopAppBar(

                                title = {

                                    Text(

                                        text = "Resolute AI Assignment",

                                        modifier = Modifier.fillMaxWidth(),
                                        // on below line we are specifying
                                        // text alignment
                                        textAlign = TextAlign.Center,

                                        color = Color.White
                                    )
                                })
                        },
                    content = { paddingValues ->
                       Box(modifier = Modifier.padding(paddingValues))
                        firebaseUI(LocalContext.current)
                    })
                }
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun firebaseUI(context: Context) {

    // on below line creating variable for course name,
    // course duration and course description.
    val phoneNumber = remember {
        mutableStateOf("")
    }

    val otp = remember {
        mutableStateOf("")
    }

    val verificationID = remember {
        mutableStateOf("")
    }

    val message = remember {
        mutableStateOf("")
    }


    var mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    Column(

        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),

        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(

            value = phoneNumber.value,

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),


            onValueChange = { phoneNumber.value = it },


            placeholder = { Text(text = "Enter your phone number") },


            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),


            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),


            singleLine = true,
        )


        Spacer(modifier = Modifier.height(10.dp))


        Button(
            onClick = {

                if (TextUtils.isEmpty(phoneNumber.value.toString())) {
                    Toast.makeText(context, "Please enter phone number..", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    val number = "+91${phoneNumber.value}"

                    sendVerificationCode(number, mAuth, context as Activity, callbacks)
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
        }


        Spacer(modifier = Modifier.height(10.dp))


        TextField(

            value = otp.value,

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            onValueChange = { otp.value = it },


            placeholder = { Text(text = "Enter your otp") },


            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),


            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),


            singleLine = true,
        )


        Spacer(modifier = Modifier.height(10.dp))


        Button(
            onClick = {

                if (TextUtils.isEmpty(otp.value.toString())) {

                    Toast.makeText(context, "Please enter otp..", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // on below line generating phone credentials.
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        verificationID.value, otp.value
                    )

                    signInWithPhoneAuthCredential(
                        credential,
                        mAuth,
                        context as Activity,
                        context,
                        message
                    )
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(text = "Verify OTP", modifier = Modifier.padding(8.dp))
        }


        Spacer(modifier = Modifier.height(5.dp))

        Text(
            // on below line displaying message for verification status.
            text = message.value,
            style = TextStyle(color = greenColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }

    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            message.value = "Verification successful"
            Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            // on below line displaying error as toast message.
            message.value = "Fail to verify user : \n" + p0.message
            Toast.makeText(context, "Verification failed..", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, p1: ForceResendingToken) {

            super.onCodeSent(verificationId, p1)
            verificationID.value = verificationId
        }
    }
}


private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    auth: FirebaseAuth,
    activity: Activity,
    context: Context,
    message: MutableState<String>
) {
    // on below line signing with credentials.
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->

            if (task.isSuccessful) {
                message.value = "Verification successful"
                Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in failed, display a message
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code
                    // entered was invalid
                    Toast.makeText(
                        context,
                        "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
}

private fun sendVerificationCode(
    number: String,
    auth: FirebaseAuth,
    activity: Activity,
    callbacks: OnVerificationStateChangedCallbacks
) {

    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(number) // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(activity) // Activity (for callback binding)
        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}
@Composable
fun Randome() {
    Text(text = "Hello World!", modifier = Modifier
        .padding(16.dp)
        .size(23.dp), style = MaterialTheme.typography.headlineSmall)

}