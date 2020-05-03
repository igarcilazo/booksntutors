package com.example.bnt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_recyclerviewtutors.*
import kotlinx.android.synthetic.main.tutoring_row.view.*

class RecycleViewTutorsActivity : AppCompatActivity() {
    /**
     * Overrides onCreate function and setup recycler view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewtutors)
        supportActionBar?.title = "Search tutors"
        recyclerView_tutors.layoutManager = LinearLayoutManager(this)
        fetchTutoring()
    }
    /**
     * Companion object to reuse data from Firebase in RequestBookActivity
     */
    companion object {

        var TUTORID = "TutorId"
        var TUTORINGTITLE = "TutoringTitle"
        var TUTORINGSUBJECT = "TutoringSubject"
        var TUTORINGDESCRIPTION = "TutoringDescription"
        var TUTORHOURLYRATE = "TutorHourlyRate"
        var ITEMID = "ItemId"
    }
    /**
     * This function fetch tutoring posts from firebase
     */
    private fun fetchTutoring() {

        val ref = FirebaseDatabase.getInstance().getReference("/tutoring")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val tutoring = it.getValue(Tutoring::class.java)
                    if (tutoring != null) {
                        adapter.add(TutoringItem(tutoring))
                    }
                    recyclerView_tutors.adapter = adapter
                }
                adapter.setOnItemClickListener { item, view ->
                    val tutoringItem = item as TutoringItem
                    val intent = Intent(view.context, RequestTutorActivity::class.java)
                    intent.putExtra(TUTORID, tutoringItem.tutoring.userid)
                    intent.putExtra(TUTORINGTITLE, tutoringItem.tutoring.title)
                    intent.putExtra(TUTORINGSUBJECT, tutoringItem.tutoring.subject)
                    intent.putExtra(TUTORINGDESCRIPTION, tutoringItem.tutoring.description)
                    intent.putExtra(TUTORHOURLYRATE, tutoringItem.tutoring.price)
                    intent.putExtra(ITEMID, tutoringItem.tutoring.itemId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun fetchTutorsByHighPrice() {

        val ref = FirebaseDatabase.getInstance().getReference("/tutoring")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val tutoring = it.getValue(Tutoring::class.java)
                    if (tutoring != null) {
                        adapter.add(TutoringItem(tutoring))
                    }
                    recyclerView_tutors.adapter = adapter
                }
                adapter.setOnItemClickListener { item, view ->
                    val tutoringItem = item as TutoringItem
                    val intent = Intent(view.context, RequestTutorActivity::class.java)
                    intent.putExtra(TUTORID, tutoringItem.tutoring.userid)
                    intent.putExtra(TUTORINGTITLE, tutoringItem.tutoring.title)
                    intent.putExtra(TUTORINGSUBJECT, tutoringItem.tutoring.subject)
                    intent.putExtra(TUTORINGDESCRIPTION, tutoringItem.tutoring.description)
                    intent.putExtra(TUTORHOURLYRATE, tutoringItem.tutoring.price)
                    intent.putExtra(ITEMID, tutoringItem.tutoring.itemId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
    private fun fetchTutorsByLowPrice() {

        val ref = FirebaseDatabase.getInstance().getReference("/tutoring").orderByChild("price")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val tutoring = it.getValue(Tutoring::class.java)
                    if (tutoring != null) {
                        adapter.add(TutoringItem(tutoring))
                    }
                    recyclerView_tutors.adapter = adapter
                }
                adapter.setOnItemClickListener { item, view ->
                    val tutoringItem = item as TutoringItem
                    val intent = Intent(view.context, RequestTutorActivity::class.java)
                    intent.putExtra(TUTORID, tutoringItem.tutoring.userid)
                    intent.putExtra(TUTORINGTITLE, tutoringItem.tutoring.title)
                    intent.putExtra(TUTORINGSUBJECT, tutoringItem.tutoring.subject)
                    intent.putExtra(TUTORINGDESCRIPTION, tutoringItem.tutoring.description)
                    intent.putExtra(TUTORHOURLYRATE, tutoringItem.tutoring.price)
                    intent.putExtra(ITEMID, tutoringItem.tutoring.itemId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun fetchTutorsByQuery(query: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/tutoring").orderByChild("title")
            .startAt(query).endAt(query + "\uf8ff")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val tutoring = it.getValue(Tutoring::class.java)
                    if (tutoring != null) {
                        adapter.add(TutoringItem(tutoring))
                    }
                    recyclerView_tutors.adapter = adapter
                }
                adapter.setOnItemClickListener { item, view ->
                    val tutoringItem = item as TutoringItem
                    val intent = Intent(view.context, RequestTutorActivity::class.java)
                    intent.putExtra(TUTORID, tutoringItem.tutoring.userid)
                    intent.putExtra(TUTORINGTITLE, tutoringItem.tutoring.title)
                    intent.putExtra(TUTORINGSUBJECT, tutoringItem.tutoring.subject)
                    intent.putExtra(TUTORINGDESCRIPTION, tutoringItem.tutoring.description)
                    intent.putExtra(TUTORHOURLYRATE, tutoringItem.tutoring.price)
                    intent.putExtra(ITEMID, tutoringItem.tutoring.itemId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    /**
     * These functions inflates the option menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                val profile = Intent(this, ProfileActivity::class.java)
                startActivity(profile)
            }
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
            R.id.sort_high_menu -> {
                fetchTutorsByHighPrice()
            }
            R.id.sort_low_menu -> {
                fetchTutorsByLowPrice()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    /**
     * These functions inflates the option menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recyclerviewbooks_menu, menu)
        val searchView = menu?.findItem(R.id.bookbar_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                fetchTutorsByQuery(query)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
/**
 * ViewHolder used for the recycler view
 */

class TutoringItem(val tutoring: Tutoring) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tutoringTitle_textView.text = tutoring.title
        viewHolder.itemView.Subject_textView.text = tutoring.subject
        viewHolder.itemView.description_textView.text = tutoring.description
        viewHolder.itemView.hourlyPrice_textView.text = "$ " + tutoring.price + "/hr"
    }

    override fun getLayout(): Int {
        return R.layout.tutoring_row
    }
}