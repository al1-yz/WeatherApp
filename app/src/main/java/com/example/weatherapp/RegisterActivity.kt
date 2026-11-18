package com.example.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.db.fb.toFBUser
import com.example.weatherapp.model.User
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }
    val activity = LocalActivity.current as Activity
    Column(
        modifier = modifier.padding(24.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        // Sem o padding da Scaffold: "modifier" já o inclui e seria aplicado a cada campo de novo.
        val fieldModifier = Modifier.fillMaxWidth(fraction = 0.9f)
        Text(
            text = "Registro",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.size(12.dp))
        OutlinedTextField(
            value = name,
            label = { Text(text = "Digite seu nome") },
            modifier = fieldModifier,
            onValueChange = { name = it }
        )
        Spacer(modifier = Modifier.size(12.dp))
        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = fieldModifier,
            onValueChange = { email = it }
        )
        Spacer(modifier = Modifier.size(12.dp))
        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha") },
            modifier = fieldModifier,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.size(12.dp))
        OutlinedTextField(
            value = repeatPassword,
            label = { Text(text = "Repita sua senha") },
            modifier = fieldModifier,
            onValueChange = { repeatPassword = it },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            modifier = fieldModifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                FBDatabase().register(User(name, email).toFBUser())
                                Toast.makeText(activity, "Registro OK!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(activity,"Registro FALHOU!", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                enabled = name.isNotEmpty() && email.isNotEmpty() &&
                    password.isNotEmpty() && repeatPassword.isNotEmpty() &&
                    password == repeatPassword
            ) {
                Text("Registrar")
            }
            Button(
                onClick = { name = ""; email = ""; password = ""; repeatPassword = "" }
            ) {
                Text("Limpar")
            }
        }
    }
}
