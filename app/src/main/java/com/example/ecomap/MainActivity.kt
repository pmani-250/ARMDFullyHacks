package com.example.ecomap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.ecomap.HomeMaps.Home

class MainActivity : AppCompatActivity() {

    private lateinit var search: Button
    private lateinit var name: EditText
    private lateinit var longitude: EditText
    private lateinit var latitude: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search = findViewById(R.id.submit)
        name = findViewById(R.id.name)
        longitude = findViewById(R.id.longitude)
        latitude = findViewById(R.id.latitude)

        setSubmitBtn()
    }

    private fun setSubmitBtn() {
        search.setOnClickListener {
            val nameText = name.text.toString()
            val long = longitude.text.toString()
            val lat = latitude.text.toString()

            val intent = Intent(this, Home::class.java).apply {
                putExtra("name", nameText)
                putExtra("longitude", long)
                putExtra("latitude", lat)
            }
            startActivity(intent)
        }
    }
}