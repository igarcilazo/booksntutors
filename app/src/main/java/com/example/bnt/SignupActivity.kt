package com.example.bnt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]
    /**
     * Overrides onCreate function and loads the signup page for creating new users
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.title = "booksNtutors"

        signup_button.setOnClickListener {
            val username = username_signup.text.toString()
            val email = email_signup.text.toString()
            val password = password_signup.text.toString()
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter Username, email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "User created.", Toast.LENGTH_SHORT).show()
                    val uid = FirebaseAuth.getInstance().uid ?: ""
                    database = FirebaseDatabase.getInstance().reference
                    val user = User(uid, username, email)
                    val login = Intent(this, MainActivity::class.java)

                    database.child("users").child(uid).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(baseContext, "user info saved", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(login)
                        }
                        .addOnFailureListener {
                            Toast.makeText(baseContext, "save user info failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
        }

        alreadyRegistered_textview.setOnClickListener {
            val login = Intent(this, MainActivity::class.java)
            startActivity(login)
        }
    }

}
/**
 * User info class to help us retrieving users from Firebase
 */
class User(val userid: String, val username: String, val email: String)

