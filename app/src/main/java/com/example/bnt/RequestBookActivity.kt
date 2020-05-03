package com.example.bnt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_requestbook.*
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RequestBookActivity : AppCompatActivity() {
    /**
     * Overrides onCreate function and retrieves selected book from Firebase
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        var sellerEmail = ""
        val storage = FirebaseStorage.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestbook)
        supportActionBar?.title = "Book Description"
        val bookname = intent.getStringExtra(RecycleViewBooksActivity.BOOKTITLE)
        RequestBookTitle_textView.text = bookname
        bookSubject_textView.text = intent.getStringExtra(RecycleViewBooksActivity.BOOKSUBJECT)
        RequestBookDescription_textView.text =
            intent.getStringExtra(RecycleViewBooksActivity.BOOKDESCRIPTION)
        RequestBookPrice_textView.text =
            "$" + intent.getStringExtra(RecycleViewBooksActivity.BOOKPRICE)
        val sellerid = intent.getStringExtra(RecycleViewBooksActivity.BOOKSELLER)?:""
        var filename = intent.getStringExtra(RecycleViewBooksActivity.BOOKIMAGENAME)
        storage.getReference("""/images/${filename}""")
            .downloadUrl.addOnCompleteListener { taskSnapshot ->
            Picasso.get().load(taskSnapshot.result.toString()).into(book_imageView)
        }

        FirebaseDatabase.getInstance().getReference("/users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userinfo = dataSnapshot.child(sellerid).getValue(Userinfo::class.java)
                    if (userinfo != null) {
                        sellerEmail = userinfo.email
                        bookSellerInfo_textView.text =
                            userinfo.username + "\n" + userinfo.email + "\n" + userinfo.phone
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, "failed to read user", Toast.LENGTH_SHORT).show()
                }
            })

        buyBook_button.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts("mailto", sellerEmail, null)
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, bookname)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I want to buy this book")
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
