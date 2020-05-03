package com.example.bnt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_recyclerviewpoststutoring.*

class RecyclerViewPostsTutoringActivity : AppCompatActivity() {
    /**
     * Overrides onCreate function and setup recycler view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewpoststutoring)
        supportActionBar?.title = "My Posts"
        this.recyclerView_postTutoring.layoutManager = LinearLayoutManager(this)
        fetchPosts()

    }
    /**
     * Companion object to reuse data from Firebase in the next intent
     */
    companion object {
        val POSTTITLE = "PostTitle"
        val POSTSUBJECT = "PostSubject"
        val POSTPRICE = "PostPrice"
        val POSTDESCRIPTION = "BookDescription"
        val POSTSELLERID = "PostSellerId"
        val ITEMID = "ItemId"
    }

    /**
     * This function fetch books for sale from an specific user from firebase
     */
    private fun fetchPosts() {
        val ref = FirebaseDatabase.getInstance().getReference("/tutoring")
        val userid = FirebaseAuth.getInstance().uid ?: ""
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val post = it.getValue(Tutoring::class.java)
                    if (post != null && post.userid == userid) {
                        adapter.add(TutoringItem(post))
                    }
                    recyclerView_postTutoring.adapter = adapter
                }

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as TutoringItem
                    val postintent = Intent(view.context, EditPostTutoringActivity::class.java)
                    postintent.putExtra(POSTSELLERID, postItem.tutoring.userid)
                    postintent.putExtra(POSTTITLE, postItem.tutoring.title)
                    postintent.putExtra(POSTSUBJECT, postItem.tutoring.subject)
                    postintent.putExtra(POSTDESCRIPTION, postItem.tutoring.description)
                    postintent.putExtra(POSTPRICE, postItem.tutoring.price)
                    postintent.putExtra(ITEMID, postItem.tutoring.itemId)
                    startActivity(postintent)
                    finish()
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
    /**
     * This function implements the options menu (signout & profile items)
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.signOut_item -> {
                FirebaseAuth.getInstance().signOut()
                val login = Intent(this, MainActivity::class.java)
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                login.putExtra("EXIT", true)
                startActivity(login)
                finish()
            }
            R.id.profile_item -> {
                val profile = Intent(this, ProfileActivity::class.java)
                startActivity(profile)
            }
            R.id.books_item -> {
                val bookPost = Intent(this, RecyclerViewPostsActivity::class.java)
                startActivity(bookPost)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    /**
     * These functions inflates the option menu
     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.myposts_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}



