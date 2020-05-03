package com.example.bnt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    /**
     * Overrides onCreate function and loads the login page for authentication
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "booksNtutors"

        login_button.setOnClickListener {
            val email = login_email_text.text.toString()
            val password = login_password_text.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Successful authentication.", Toast.LENGTH_SHORT).show()
                        val homeIntent = Intent(this, HomeActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        dontHaveAccount_textview.setOnClickListener {
            val signUp = Intent(this, SignupActivity::class.java)
            startActivity(signUp)
        }
    }
}
