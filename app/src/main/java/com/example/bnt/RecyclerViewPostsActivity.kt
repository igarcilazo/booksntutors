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
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_recyclerviewposts.*
import kotlinx.android.synthetic.main.posts_row.view.*

class RecyclerViewPostsActivity : AppCompatActivity() {
    /**
     * Overrides onCreate function and setup recycler view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewposts)
        supportActionBar?.title = "My Posts"
        this.recyclerView_posts.layoutManager = LinearLayoutManager(this)
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
        val POSTFILENAME = "fileName"
    }

    /**
     * This function fetch books for sale from an specific user from firebase
     */
    private fun fetchPosts() {
        val ref = FirebaseDatabase.getInstance().getReference("/sellbooks")
        val userid = FirebaseAuth.getInstance().uid ?: ""
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val post = it.getValue(Post::class.java)
                    if (post != null && post.userid == userid) {
                        adapter.add(PostItem(post))
                    }
                    recyclerView_posts.adapter = adapter
                }

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as PostItem
                    val postintent = Intent(view.context, EditPostActivity::class.java)
                    postintent.putExtra(POSTSELLERID, postItem.post.userid)
                    postintent.putExtra(POSTTITLE, postItem.post.title)
                    postintent.putExtra(POSTSUBJECT, postItem.post.subject)
                    postintent.putExtra(POSTDESCRIPTION, postItem.post.description)
                    postintent.putExtra(POSTPRICE, postItem.post.price)
                    postintent.putExtra(ITEMID, postItem.post.itemid)
                    postintent.putExtra(POSTFILENAME, postItem.post.filename)
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
            R.id.tutoring_item -> {
                val tutoringPost = Intent(this, RecyclerViewPostsTutoringActivity::class.java)
                startActivity(tutoringPost)

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

/**
 * ViewHolder used for the recycler view
 */

class PostItem(val post: Post) : Item<ViewHolder>() {
    val storage = FirebaseStorage.getInstance()

    override fun bind(viewHolder: ViewHolder, position: Int) {

        var location = "/images/"+post.filename
        storage.getReference(location)
            .downloadUrl.addOnCompleteListener { taskSnapshot ->

            Picasso.get().load(taskSnapshot.result.toString())
                .into(viewHolder.itemView.book_imageView)

        }

        viewHolder.itemView.postTitle_textView.text = post.title
        viewHolder.itemView.postSubject_textView.text = post.subject
        viewHolder.itemView.postDescription_textView.text = post.description
        viewHolder.itemView.postPrice_textView.text = "$" + post.price
    }

    override fun getLayout(): Int {
        return R.layout.posts_row
    }
}
/**
 * Post class to help us retrieving posts from Firebase
 */
class Post(
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


