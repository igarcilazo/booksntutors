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
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recyclerviewbooks.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.book_row.view.*


class RecycleViewBooksActivity : AppCompatActivity() {


    /**
     * Overrides onCreate function and setup recycler view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewbooks)
        supportActionBar?.title = "Books for sale"
        recyclerView_books.layoutManager = LinearLayoutManager(this)
        fetchBooks()
    }
    /**
     * Companion object to reuse data from Firebase in the next intent
     */

    companion object {
        val BOOKTITLE = "BookTitle"
        val BOOKSUBJECT = "BookSubject"
        val BOOKPRICE = "BookPrice"
        val BOOKDESCRIPTION = "BookDescription"
        val BOOKIMAGENAME = "BookImageName"
        val BOOKSELLER = "BookSeller"
        val ITEMID = "BookId"
    }
    /**
     * This function fetch books for sale from firebase
     */

    private fun fetchBooks() {

        var ref = FirebaseDatabase.getInstance().getReference("/sellbooks")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null) {
                        adapter.add(BookItem(book))
                    }
                    recyclerView_books.adapter = adapter
                }

                adapter.setOnItemClickListener { item, view ->
                    val bookItem = item as BookItem
                    val bookintent = Intent(view.context, RequestBookActivity::class.java)
                    bookintent.putExtra(BOOKTITLE, bookItem.book.title)
                    bookintent.putExtra(BOOKSUBJECT, bookItem.book.subject)
                    bookintent.putExtra(BOOKPRICE, bookItem.book.price)
                    bookintent.putExtra(BOOKDESCRIPTION, bookItem.book.description)
                    bookintent.putExtra(BOOKIMAGENAME, bookItem.book.filename)
                    bookintent.putExtra(BOOKSELLER, bookItem.book.userid)
                    bookintent.putExtra(ITEMID, bookItem.book.itemid)
                    startActivity(bookintent)
                    finish()
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
    /**
     * This function fetch books or sale by query from firebase. It use with the search bar
     */
    private fun fetchBooksByQuery(query: String) {
        var ref = FirebaseDatabase.getInstance().getReference("/sellbooks").orderByChild("title")
            .startAt(query)
            .endAt(query + "\uf8ff")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null) {
                        adapter.add(BookItem(book))
                    }
                    recyclerView_books.adapter = adapter
                }

                adapter.setOnItemClickListener { item, view ->
                    val bookItem = item as BookItem
                    val bookintent = Intent(view.context, RequestBookActivity::class.java)
                    bookintent.putExtra(BOOKTITLE, bookItem.book.title)
                    bookintent.putExtra(BOOKSUBJECT, bookItem.book.subject)
                    bookintent.putExtra(BOOKPRICE, bookItem.book.price)
                    bookintent.putExtra(BOOKDESCRIPTION, bookItem.book.description)
                    bookintent.putExtra(BOOKIMAGENAME, bookItem.book.filename)
                    bookintent.putExtra(BOOKSELLER, bookItem.book.userid)
                    bookintent.putExtra(ITEMID, bookItem.book.itemid)
                    startActivity(bookintent)
                    finish()
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }
    /**
     * This function sorts books for sale by lower price from firebase
     */
    private fun fetchBooksByLowPrice() {
        var ref = FirebaseDatabase.getInstance().getReference("/sellbooks").orderByChild("price")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null) {
                        adapter.add(BookItem(book))
                    }
                    recyclerView_books.adapter = adapter
                }

                adapter.setOnItemClickListener { item, view ->
                    val bookItem = item as BookItem
                    val bookintent = Intent(view.context, RequestBookActivity::class.java)
                    bookintent.putExtra(BOOKTITLE, bookItem.book.title)
                    bookintent.putExtra(BOOKSUBJECT, bookItem.book.subject)
                    bookintent.putExtra(BOOKPRICE, bookItem.book.price)
                    bookintent.putExtra(BOOKDESCRIPTION, bookItem.book.description)
                    bookintent.putExtra(BOOKIMAGENAME, bookItem.book.filename)
                    bookintent.putExtra(BOOKSELLER, bookItem.book.userid)
                    bookintent.putExtra(ITEMID, bookItem.book.itemid)
                    startActivity(bookintent)
                    finish()
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }
    /**
     * This function sorts books for sale by higher price from firebase
     */

    private fun fetchBooksByHighPrice() {
        var ref = FirebaseDatabase.getInstance().getReference("/sellbooks").orderByChild("price").limitToFirst(20)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null) {
                        adapter.add(BookItem(book))
                    }
                    recyclerView_books.adapter = adapter
                }

                adapter.setOnItemClickListener { item, view ->
                    val bookItem = item as BookItem
                    val bookintent = Intent(view.context, RequestBookActivity::class.java)
                    bookintent.putExtra(BOOKTITLE, bookItem.book.title)
                    bookintent.putExtra(BOOKSUBJECT, bookItem.book.subject)
                    bookintent.putExtra(BOOKPRICE, bookItem.book.price)
                    bookintent.putExtra(BOOKDESCRIPTION, bookItem.book.description)
                    bookintent.putExtra(BOOKIMAGENAME, bookItem.book.filename)
                    bookintent.putExtra(BOOKSELLER, bookItem.book.userid)
                    bookintent.putExtra(ITEMID, bookItem.book.itemid)
                    startActivity(bookintent)
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
                fetchBooksByHighPrice()
            }
            R.id.sort_low_menu -> {
                fetchBooksByLowPrice()
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
                fetchBooksByQuery(query)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
/**
 * ViewHolder used for the recycler view
 */

class BookItem(val book: Book) : Item<ViewHolder>() {
    val storage = FirebaseStorage.getInstance()

    override fun bind(viewHolder: ViewHolder, position: Int) {

        var location = "/images/"+book.filename
        storage.getReference(location)
            .downloadUrl.addOnCompleteListener { taskSnapshot ->

            Picasso.get().load(taskSnapshot.result.toString())
                .into(viewHolder.itemView.book_imageView)

        }
        viewHolder.itemView.textView_bookTitle.text = book.title
        viewHolder.itemView.textView_bookDescription.text = book.description
        viewHolder.itemView.textView_price.text = "$" + book.price
    }

    override fun getLayout(): Int {
        return R.layout.book_row
    }


}