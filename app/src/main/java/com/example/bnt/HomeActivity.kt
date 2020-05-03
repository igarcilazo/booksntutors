package com.example.bnt
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    /**
     * Overrides onCreate function and setup the home menu buttons
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = "Home"
        showBooks_button.setOnClickListener {
            val books = Intent(this, RecycleViewBooksActivity::class.java)
            startActivity(books)
        }
        sellBooks_button.setOnClickListener {
            val sellBooks = Intent(this, SellBooksActivity::class.java)
            startActivity(sellBooks)
        }
        showTutors_button.setOnClickListener {
            val showTutors = Intent(this, RecycleViewTutorsActivity::class.java)
            startActivity(showTutors)
        }
        offerTutoring_button.setOnClickListener {
            val offerTutoring = Intent(this, OfferTutoringActivity::class.java)
            startActivity(offerTutoring)
        }
        profile_button.setOnClickListener {
            val profile = Intent(this, ProfileActivity::class.java)
            startActivity(profile)
        }
        myPosts_button.setOnClickListener {
            val posts = Intent(this, RecyclerViewPostsActivity::class.java)
            startActivity(posts)
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