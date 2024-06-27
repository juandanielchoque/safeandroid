package com.miempresa.safeband

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("phoneNumbers")

        val phoneNumberEditText: EditText = findViewById(R.id.phoneNumberEditText)
        val submitButton: Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            if (phoneNumber.isNotEmpty()) {
                // Save phone number to Firebase
                val id = database.push().key
                if (id != null) {
                    database.child(id).setValue(phoneNumber).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Numero de telefono recibido :)", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al subir numero :(", Toast.LENGTH_SHORT).show()
                            Log.e("Firebase", "Error al subir número: ${task.exception?.message}")
                            task.exception?.printStackTrace()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un numero", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
