package com.example.ecomap.HomeMaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ecomap.R

class Home : AppCompatActivity() {

    private var name: String = ""
    private var city: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        name = bundle?.getString("name")!!
        city = bundle?.getString("city")!!


        Log.d("HomeIntent", name+" "+city)
    }
}