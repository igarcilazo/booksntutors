package com.example.bnt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_requesttutor.*

class RequestTutorActivity : AppCompatActivity() {

    /**
     * Overrides onCreate function and retrieves selected tutoring service from Firebase
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        var tutorEmail = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requesttutor)
        supportActionBar?.title = "Tutor description"
        val tutoringTitle = intent.getStringExtra(RecycleViewTutorsActivity.TUTORINGTITLE)
        RequestTutorTitle_textView.text = tutoringTitle
        RequestTutorSubject_textView.text =
            intent.getStringExtra(RecycleViewTutorsActivity.TUTORINGSUBJECT)
        RequestTutorDescription_textView.text =
            intent.getStringExtra(RecycleViewTutorsActivity.TUTORINGDESCRIPTION)
        RequestTutorHourlyRate_textView.text =
            "$ " + intent.getStringExtra(RecycleViewTutorsActivity.TUTORHOURLYRATE) + "/hr"
        val tutorid = intent.getStringExtra(RecycleViewTutorsActivity.TUTORID)?:""
        FirebaseDatabase.getInstance().getReference("/users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userinfo = dataSnapshot.child(tutorid).getValue(Userinfo::class.java)
                    if (userinfo != null) {
                        tutorEmail = userinfo.email
                        tutorInfo_textView.text =
                            userinfo.username + "\n" + userinfo.email + "\n" + userinfo.phone
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, "failed to read user", Toast.LENGTH_SHORT).show()
                }
            })


        requestTutor_button.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts("mailto", tutorEmail, null)
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, tutoringTitle)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I want to get tutoring")
            startActivity(Intent.createChooser(emailIntent, "Send email"))
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