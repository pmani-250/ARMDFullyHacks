package com.example.ecomap.HomeMaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.ecomap.R
import org.w3c.dom.Text

class Home : AppCompatActivity() {

    private var name: String = ""
    private var longitude: String = ""
    private var latitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        name = bundle?.getString("name")!!
        longitude = bundle?.getString("longitude")!!
        latitude = bundle?.getString("latitude")!!

        val nameText = findViewById<TextView>(R.id.username)
        nameText.text = name

        val coords = arrayOf(longitude, latitude)

        val fragment = MapsFragment.newInstance(coords)
        supportFragmentManager.beginTransaction()
            .replace(R.id.maps, fragment)
            .commit()


        Log.d("HomeIntent", name+" "+longitude)

    }
}