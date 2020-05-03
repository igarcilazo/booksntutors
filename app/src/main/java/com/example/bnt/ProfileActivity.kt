package com.example.bnt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {
    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]
    /**
     * Overrides onCreate function and retrieves user from Firebase
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile"
        database = FirebaseDatabase.getInstance().reference
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("/users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userinfo = dataSnapshot.child(currentuser).getValue(Userinfo::class.java)
                    if (userinfo != null)
                        LoadUserInfo(userinfo)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, "failed to read user", Toast.LENGTH_SHORT).show()
                }
            })
        save_button.setOnClickListener {
            val phone = phone_textView.text.toString()
            database.child("users").child(currentuser).child("phone").setValue(phone)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "Changes saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Changes failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
    /**
     * This function loads user info to screen
     */
    private fun LoadUserInfo(userinfo: Userinfo) {
        name_textView.text = userinfo.username
        email_textView.text = userinfo.email
        phone_textView.setText(userinfo.phone)
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
 * User info class to help us retrieving users from Firebase
 */

class Userinfo(val username: String, val email: String, val phone: String) {
    constructor() : this("", "", "")
}
