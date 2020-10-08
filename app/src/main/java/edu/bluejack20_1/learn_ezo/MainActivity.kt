package edu.bluejack20_1.learn_ezo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD:app/src/main/java/com/example/tpa_mobile/MainActivity.kt
import android.os.Handler
=======
import edu.bluejack20_1.learn_ezo.R
>>>>>>> 5fd8b26f31aa80eac2899c2e4e3ca1dddde0879f:app/src/main/java/edu/bluejack20_1/learn_ezo/MainActivity.kt

class MainActivity : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}