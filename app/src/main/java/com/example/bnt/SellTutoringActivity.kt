package com.example.bnt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_selltutoring.*


class OfferTutoringActivity : AppCompatActivity() {
    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]
    /**
     * Overrides onCreate function and saves tutoring service to firebase
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selltutoring)
        supportActionBar?.title = "Offer tutoring"
        database = FirebaseDatabase.getInstance().reference

        postTutoring.setOnClickListener {
            val itemId = database.child("tutoring").push().getKey() ?: ""
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val tutoringTitle = SellTutoringTitle_textview.text.toString()
            val subject = SellTutoringSubject_textview.text.toString()
            val description = SellTutoringDescription_textview.text.toString()
            val hourlyrate = SellTutoringHourlyRate_textView.text.toString()
            val tutoringPosting =
                Tutoring(uid, tutoringTitle, subject, description, hourlyrate, itemId)
            val homeIntent = Intent(this, HomeActivity::class.java)
            if (itemId != "") {
                database.child("tutoring").child(itemId).setValue(tutoringPosting)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext, "tutoring post in database", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(homeIntent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(baseContext, "tutoring post failed", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }
    /**
     * This function implements the options menu (signout & profile items)
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOut_item) {
            FirebaseAuth.getInstance().signOut()
            val login = Intent(this, MainActivity::class.java)
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            login.putExtra("EXIT", true)
            startActivity(login)
            finish()
        }
        if (item.itemId == R.id.profile_item){
            val profile = Intent(this, ProfileActivity::class.java)
            startActivity(profile)
        }

        return super.onOptionsItemSelected(item)
    }
    /**
     * These functions inflates the option menu
     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
/**
 * Tutoring info class to help us saving tutoring services to Firebase
 */
class Tutoring(
    val userid: String,
    val title: String,
    val subject: String,
    val description: String,
    val price: String,
    val itemId: String
) {
    constructor() : this("", "", "", "", "", "")
}
