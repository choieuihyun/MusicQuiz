package com.enjoy_project.musicquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_info_input)

        val name = findViewById<EditText>(R.id.name)
        val team = findViewById<EditText>(R.id.team)
        val inputButton = findViewById<Button>(R.id.inputButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        inputButton.setOnClickListener {

            val userName = name.text.toString()
            val userTeam = team.text.toString()
            val userCount = 0

            CoroutineScope(Dispatchers.IO).launch {

                val retrofit = RetrofitImpl()

                try {
                    retrofit.addUser(null, userName, userCount, userTeam)
                    Log.d("InfoInput", "success")
                } catch (e: ArithmeticException) {
                    Log.d("PostError", e.message.toString())
                }

            }

        }

        nextButton.setOnClickListener {

            val intent = Intent(this, TwentyTwentyMusicActivity::class.java)
            startActivity(intent)

        }
        

    }

}