package com.example.bnt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editpost.*

class EditPostTutoringActivity : AppCompatActivity() {
    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]

    /**
     * Overrides onCreate function and retrieves books info from previous recyclerView.
     * Retrieves tutoring info from Firebase
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editpost)
        supportActionBar?.title = "Edit Post"
        database = FirebaseDatabase.getInstance().reference
        val userid = intent.getStringExtra(RecyclerViewPostsTutoringActivity.POSTSELLERID) ?: ""
        val itemid = intent.getStringExtra(RecyclerViewPostsTutoringActivity.ITEMID) ?: ""
        FirebaseDatabase.getInstance().getReference("/tutoring")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val postInfo = dataSnapshot.child(itemid).getValue(Tutoring::class.java)
                    if (postInfo != null)
                        loadPost(postInfo)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, "failed to read user", Toast.LENGTH_SHORT).show()
                }
            })

        saveChanges_button.setOnClickListener {
            val title = title_textView.text.toString()
            val subject = subject_textView.text.toString()
            val description = description_textView.text.toString()
            val price = price_textView.text.toString()
            val tutoringupdate = Tutoring(userid, title, subject, description, price, itemid)
            val homeIntent = Intent(this, HomeActivity::class.java)
            database.child("tutoring").child(itemid).setValue(tutoringupdate).addOnSuccessListener {
                Toast.makeText(baseContext, "Changes saved", Toast.LENGTH_SHORT).show()
                startActivity(homeIntent)
            }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Changes failed", Toast.LENGTH_SHORT).show()
                }
        }

        delete_button.setOnClickListener {

            database.child("sellbooks").child(itemid).removeValue().addOnSuccessListener {
                Toast.makeText(baseContext, "Post deleted", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Post deletion failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /**
     * This function loads tutoring description
     */

    private fun loadPost(tutoringInfo: Tutoring) {
        title_textView.setText(tutoringInfo.title)
        subject_textView.setText(tutoringInfo.subject)
        description_textView.setText(tutoringInfo.description)
        price_textView.setText(tutoringInfo.price)
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
        if (item.itemId == R.id.profile_item) {
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
