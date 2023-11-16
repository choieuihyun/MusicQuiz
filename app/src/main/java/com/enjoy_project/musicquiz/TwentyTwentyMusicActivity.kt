package com.enjoy_project.musicquiz

import android.media.AsyncPlayer
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TwentyTwentyMusicActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_twenty_music)

        val playButton = findViewById<Button>(R.id.playButton)

        playButton.setOnClickListener {

            mediaPlayer = MediaPlayer()

        }




    }
}