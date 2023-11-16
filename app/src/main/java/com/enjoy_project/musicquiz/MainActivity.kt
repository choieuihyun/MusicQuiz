package com.enjoy_project.musicquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val twentyTwentyButton = findViewById<Button>(R.id.twentyMusic)
        val tenToTwentyButton = findViewById<Button>(R.id.tenToTwentyMusic)
        val tenButton = findViewById<Button>(R.id.tenMusic)

        twentyTwentyButton.setOnClickListener {

            val intent = Intent(this, InfoInputActivity::class.java)
            startActivity(intent)

        }



    }
}