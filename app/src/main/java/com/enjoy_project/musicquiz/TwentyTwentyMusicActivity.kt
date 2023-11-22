package com.enjoy_project.musicquiz

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class TwentyTwentyMusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    private val secondQuizRoot = "secondMusicQuiz" // firebase storage 경로 이거 숨겨야함.
    private val storage = FirebaseStorage.getInstance()
    private var storageRef = storage.reference
    private lateinit var songRef: StorageReference

    private val retrofit = RetrofitImpl()

    //private val songRef = storageRef.child("$secondQuizRoot/ditto.mp3")

    private var isPlaying = false;

    private var playingMusicId = 1;

    private lateinit var firstExample: TextView
    private lateinit var secondExample: TextView
    private lateinit var thirdExample: TextView
    private lateinit var fourthExample: TextView
    private lateinit var fifthExample: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_twenty_music)

        val playButton = findViewById<Button>(R.id.playButton)
        val nextButton = findViewById<Button>(R.id.nextMusicButton)

        firstExample = findViewById<TextView>(R.id.firstExample)
        secondExample = findViewById<TextView>(R.id.secondExample)
        thirdExample = findViewById<TextView>(R.id.thirdExample)
        fourthExample = findViewById<TextView>(R.id.fourthExample)
        fifthExample = findViewById<TextView>(R.id.fifthExample)

        playButton.setOnClickListener {

            playSongSetting(playButton)

        }

        nextButton.setOnClickListener {

            if(!isPlaying)
                playingMusicId++

        }

        dialogButton.setOnClickListener {

            customDialog(
                firstExample.text.toString(),
                secondExample.text.toString(),
                thirdExample.text.toString(),
                fourthExample.text.toString(),
                fifthExample.text.toString()
            )

        }

    }


    override fun onDestroy() {
        super.onDestroy()

        releaseMediaPlayer()

    }

    private fun playSongSetting(button: Button) {

        // 1. 배열에 노래 파일 이름들을 저장해 놓는다.
        // 2. 그냥 파이어베이스 스토리지에서 리스트로 받아온 후 하나씩 넣는다.
        // 3. maria DB에 해당 음악파일의 메타 데이터를 저장하고 그 데이터를 기반으로 스토리지에서 불러온다 --> 이게 답인듯

        CoroutineScope(Dispatchers.IO).launch {

            try {

                retrofit.getSong(playingMusicId) {

                    // 한글 파일을 못불러와서 해봤는데 쩝..
                    val encodedTitle = URLEncoder.encode(it?.title + ".mp3", "UTF-8")

                    playSong(button, getSongRef(it?.title ?: "null"))

                    // 이렇게 하면 안되고 String.xml에 저장해놓고 해야함.
                    // 이것도 백그라운드 스레드에서 할게 아님.

                    firstExample.text = ("1. " + it?.question1)
                    secondExample.text = ("2. " + it?.question2)
                    thirdExample.text = ("3. " + it?.question3)
                    fourthExample.text = ("4. " + it?.question4)
                    fifthExample.text = ("5. " + it?.question5)

                }

            } catch (e: ArithmeticException) {
                Log.d("PostError", e.message.toString())
            }
        }

    }

    private fun playSong(button: Button, ref: StorageReference) {

        ref.downloadUrl.addOnSuccessListener { uri ->
            try {

                CoroutineScope(Dispatchers.Main).launch {

                    releaseMediaPlayer()
                    Log.d("ssbal", uri.toString())

                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mediaPlayer?.setDataSource(uri.toString())
                    mediaPlayer?.prepare()

                    mediaPlayer?.setOnCompletionListener {
                        isPlaying = false
                        button.setBackgroundResource(R.drawable.music_play) // 노래가 종료되면 원래 이미지
                    }

                    buttonState(button)

                }

            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("TAG", "Media player preparation failed: ${e.message}")
            }

        }.addOnFailureListener { exception ->
            Log.e("TAG", "Streaming URL retrieval failed: ${exception.message}")

        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                pause()
            }
            release()
        }
        mediaPlayer = null
    }

    private fun getSongRef(root: String): StorageReference {
        songRef = storageRef.child("$secondQuizRoot/$root.mp3")
        Log.d("sibal", "$secondQuizRoot/$root.mp3")
        return songRef
    }

    private fun buttonState(button: Button) {

        isPlaying = if (isPlaying) {
            mediaPlayer?.pause()
            button.setBackgroundResource(R.drawable.music_play) // 정지 상태일 때 클릭
            false
        } else {
            mediaPlayer?.start()
            button.setBackgroundResource(R.drawable.music_pause) // 재생 중일 때 클릭
            true
        }

    }

    private fun customDialog(question1: String,
                             question2: String,
                             question3: String,
                             question4: String,
                             question5: String) {

        val dialog = CustomDialog(this) {
            Log.d("sdfsdf","sdfsdf")
        }

        try {

            dialog.setData(question1, question2, question3, question4, question5)

        } catch (e: NullPointerException) {

            Toast.makeText(this,"노래를 재생시키고 켜주세요", Toast.LENGTH_LONG).show()

        }

        dialog.show()

    }
}