package com.enjoy_project.musicquiz.user_ranking

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enjoy_project.musicquiz.R
import com.enjoy_project.musicquiz.RetrofitImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRankingActivity : AppCompatActivity() {

    private lateinit var userRankingAdapter: UserRankingAdapter
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var mediaPlayer: MediaPlayer
    private val retrofit = RetrofitImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ranking)

        userRecyclerView = findViewById(R.id.userRankingRecyclerView)

        setupRecyclerView(this)

        updateRecyclerView()

        mediaPlayer = MediaPlayer.create(this, R.raw.endingsong)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.release()
    }

    private fun setupRecyclerView(context: Context) {

        userRankingAdapter = UserRankingAdapter()
        userRecyclerView.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = userRankingAdapter

        }


    }

    private fun updateRecyclerView() {

        CoroutineScope(Dispatchers.IO).launch {

            retrofit.getListDescCount {

                userRankingAdapter.submitList(it)

            }

        }



    }



}