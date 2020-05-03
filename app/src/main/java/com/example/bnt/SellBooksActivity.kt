package com.example.bnt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sellbooks.*
import java.util.*

@Suppress("DEPRECATION")
class SellBooksActivity : AppCompatActivity() {

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]

    /**
     * Overrides onCreate function and selects image from android storage folder
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sellbooks)
        supportActionBar?.title = "Sell books"

        database = FirebaseDatabase.getInstance().reference

        /**
         * found this code online that helped me load images from android image folder
         */
        uploadPicture_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        post_button.setOnClickListener {
            saveBookToFirebaseDatabase()
        }

    }

    /**
     * found this code online that helped me load images from android image folder
     */

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            bookPic_imageview.setImageBitmap(bitmap)
        }
    }

    /**
     * This function saves book and book image to Firebase
     */
    private fun saveBookToFirebaseDatabase() {

        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Successfully uploaded image", Toast.LENGTH_SHORT)
                    .show()
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("sellBookActivity", "File Location: $it")
                }
            }
            .addOnFailureListener {
                Toast.makeText(baseContext, "Image upload failed", Toast.LENGTH_SHORT).show()
            }

        val bookId = database.child("sellbooks").push().getKey()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val bookname = SellbookTitle_textview.text.toString()
        val classname = SellBookSubject_textView.text.toString()
        val description = SellbookDescription_textview.text.toString()
        val price = SellBookPrice_textView.text.toString()
        val bookPosting = Book(uid, bookname, classname, description, price, filename, bookId ?: "")
        val homeIntent = Intent(this, HomeActivity::class.java)
        if (bookId != null) {
            database.child("sellbooks").child(bookId).setValue(bookPosting)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "book in database", Toast.LENGTH_SHORT).show()
                    startActivity(homeIntent)
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "book post failed", Toast.LENGTH_SHORT).show()
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
 * Book info class to help us saving books for sale to Firebase
 */
class Book(
    val userid: String,
    val title: String,
    val subject: String,
    val description: String,
    val price: String,
    val filename: String,
    val itemid: String
) {
    constructor() : this("", "", "", "", "", "", "")
}
