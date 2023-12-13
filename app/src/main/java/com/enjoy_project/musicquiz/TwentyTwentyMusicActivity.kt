package com.enjoy_project.musicquiz

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enjoy_project.musicquiz.user_ranking.UserRankingActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import java.net.URLEncoder
import java.util.SortedMap

class TwentyTwentyMusicActivity : AppCompatActivity(),
    CustomDialog.OnDataPassDialogToActivityListener {

    private var mediaPlayer: MediaPlayer? = null

    private val secondQuizRoot = "secondMusicQuiz" // firebase storage 경로 이거 숨겨야함.
    private val storage = FirebaseStorage.getInstance()
    private var storageRef = storage.reference
    private lateinit var songRef: StorageReference

    private lateinit var dialog: CustomDialog

    private var answerUserList = arrayListOf<String>() // 정답자 목록

    private val retrofit = RetrofitImpl()

    private var isPlaying = false; // mediaPlayer 시작, 정지를 위한 변수

    private var playingMusicId = 1; // 1번 문제부터 시작하기 위한 Id
    private var playingLastMusicId = 50;

    private lateinit var firstExample: TextView // 첫번째 예문
    private lateinit var secondExample: TextView // 두번째 예문
    private lateinit var thirdExample: TextView // 세번째 예문
    private lateinit var fourthExample: TextView // 네번째 예문
    private lateinit var fifthExample: TextView // 다섯번째 예문
    private lateinit var exampleAnswer: String // 정답
    private lateinit var lyricsExample: TextView // 가사 예문
    private lateinit var artistExample: TextView // 가수, 노래 이름
    private lateinit var answerUserListText: TextView // 정답자 목록

    private lateinit var redPoint: TextView // 빨간색 유저(첫번째 유저)
    private lateinit var bluePoint: TextView // 파란색 유저(두번째 유저)
    private lateinit var greenPoint: TextView // 초록색 유저(세번째 유저)
    private lateinit var yellowPoint: TextView // 노란색 유저(네번째 유저)
    private lateinit var purplePoint: TextView // 보라색 유저(다섯번째 유저)
    private lateinit var blackPoint: TextView // 검정색 유저(여섯번째 유저)
    private lateinit var orangePoint: TextView // 오렌지색 유저(일곱번째 유저)
    private lateinit var brownPoint: TextView // 갈색 유저(여덟번째 유저)

/*    private val receiver = object : BroadcastReceiver() {
        // broadCastReceiver로 실시간 Point 데이터 갱신해보려다가 실패함.
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "point-updated") {
                Log.d("BroadcastReceiver", "onReceive: Received broadcast")
                val countHashMap = intent.getSerializableExtra("countHashMap") as? SortedMap<Int?, Int?>
                if (countHashMap != null) {
                    handleResult(countHashMap)
                }
            }
        }
    }*/

    private var userList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_twenty_music)

        val playButton = findViewById<Button>(R.id.playButton)
        val nextButton = findViewById<Button>(R.id.nextMusicButton)
        val dialogButton = findViewById<Button>(R.id.dialogButton)
        val answerButton = findViewById<Button>(R.id.answerButton)

        firstExample = findViewById(R.id.firstExample)
        secondExample = findViewById(R.id.secondExample)
        thirdExample = findViewById(R.id.thirdExample)
        fourthExample = findViewById(R.id.fourthExample)
        fifthExample = findViewById(R.id.fifthExample)
        answerUserListText = findViewById(R.id.answerUserList)

        lyricsExample = findViewById(R.id.lyrics)
        artistExample = findViewById(R.id.artist)

        redPoint = findViewById(R.id.red)
        bluePoint = findViewById(R.id.blue)
        greenPoint = findViewById(R.id.green)
        yellowPoint = findViewById(R.id.yellow)
        purplePoint = findViewById(R.id.purple)
        blackPoint = findViewById(R.id.black)
        orangePoint = findViewById(R.id.orange)
        brownPoint = findViewById(R.id.brown)

        val userCount = intent.getIntExtra("teamNumber", 0) // 총 유저 수
        val userTeamName = intent.getStringExtra("teamName") // 입력 받은 팀명

        if (userTeamName != null) {

            fetchDataAndProcess(userTeamName)

        }

        playButton.setOnClickListener {

            playSongSetting(playButton)

        }

        nextButton.setOnClickListener {

            if (!isPlaying && playingMusicId < playingLastMusicId) {
                playingMusicId++
            } else if (playingMusicId == playingLastMusicId) {
                val intent = Intent(this, UserRankingActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"마지막 곡입니다.", Toast.LENGTH_LONG).show()
            }


            initializeExampleColor()
            initializeAnswerUserList()
            initializeSongsInfo()

        }

        dialogButton.setOnClickListener {

            try {

                customDialog(
                    userCount,
                    userTeamName.toString(),
                    firstExample.text.toString(),
                    secondExample.text.toString(),
                    thirdExample.text.toString(),
                    fourthExample.text.toString(),
                    fifthExample.text.toString(),
                    exampleAnswer
                )
            } catch (e : Exception) {
                Toast.makeText(this,"재생 버튼을 누르고 켜주세요", Toast.LENGTH_LONG).show()
            }

        }

        answerButton.setOnClickListener {

            try {
                val exampleArray = arrayListOf<TextView>()
                exampleArray.add(firstExample)
                exampleArray.add(secondExample)
                exampleArray.add(thirdExample)
                exampleArray.add(fourthExample)
                exampleArray.add(fifthExample)

                for (example in exampleArray) {

                    if (example.text.substring(3) == exampleAnswer)
                        example.setTextColor(getColor(R.color.red))

                }
                retrofit.getUserCount(userList) {

                    handleResult(it)

                }

                getSongsInfo()

                // 이렇게 하면 안되는거 아는데 걍..
                answerUserListText.text =
                    "정답자 : ".plus(Util.removeBrackets(answerUserList.toString()))

            } catch (e: Exception) {
                Toast.makeText(this, "노래를 재생시키고 눌러주세요", Toast.LENGTH_LONG).show()
            }

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

                    // 한글 파일을 못불러와서 해봤는데 쩝.. 그냥 노래 파일 이름을 영어로 바꿔버림.
                    val encodedTitle = URLEncoder.encode(it?.title + ".mp3", "UTF-8")

                    playSong(button, getSongRef(it?.title ?: "null"))
                    exampleAnswer = it?.answer ?: "null"
                    Log.d("answer", it?.answer.toString())

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

    private fun initializeExampleColor() {

        firstExample.setTextColor(getColor(R.color.black))
        secondExample.setTextColor(getColor(R.color.black))
        thirdExample.setTextColor(getColor(R.color.black))
        fourthExample.setTextColor(getColor(R.color.black))
        fifthExample.setTextColor(getColor(R.color.black))

    }

    // 정답자 리스트 초기화
    private fun initializeAnswerUserList() {

        answerUserList.clear()
        answerUserListText.text = answerUserList.toString()

    }

    // 노래 정보 초기화
    private fun initializeSongsInfo() {

        artistExample.text = ""

    }


    // 호출해보면 count와 유저의 순서가 안맞아. 아마 id를 같이 불러와서 순서대로 저장해야하나?
    private fun fetchDataAndProcess(teamName: String) {

        CoroutineScope(Dispatchers.IO).launch {

            retrofit.getUserListByTeam(teamName) { userList ->

                Log.d("getUserList", userList.toString())

                this@TwentyTwentyMusicActivity.userList = userList as ArrayList<String>

                retrofit.getUserCount(userList) {

                    handleResult(it)
                    Log.d("getUserCountList", it.toString())

                }

            }

        }

    }

    private fun getSongsInfo() {

        CoroutineScope(Dispatchers.IO).launch {

            retrofit.getSong(playingMusicId) {

                Log.d("title", it?.titleKr.plus(" - ").plus(it?.artist))
                artistExample.text = it?.titleKr.plus("-").plus(it?.artist)

            }

        }

    }

    private fun handleResult(countHashMap: SortedMap<Int?, Int?>) {

        // 각 유저에 해당하는 색에 대한 정답 카운트 처리
        val redPointValue = countHashMap.entries.firstOrNull()
        val bluePointValue = countHashMap.entries.elementAtOrNull(1)
        val greenPointValue = countHashMap.entries.elementAtOrNull(2)
        val yellowPointValue = countHashMap.entries.elementAtOrNull(3)
        val purplePointValue = countHashMap.entries.elementAtOrNull(4)
        val blackPointValue = countHashMap.entries.elementAtOrNull(5)
        val orangePointValue = countHashMap.entries.elementAtOrNull(6)
        val brownPointValue = countHashMap.entries.elementAtOrNull(7)

        redPoint.text = redPointValue?.value.toString()
        bluePoint.text = bluePointValue?.value.toString()
        greenPoint.text = greenPointValue?.value.toString()
        yellowPoint.text = yellowPointValue?.value.toString()
        purplePoint.text = purplePointValue?.value.toString()
        blackPoint.text = blackPointValue?.value.toString()
        orangePoint.text = orangePointValue?.value.toString()
        brownPoint.text = brownPointValue?.value.toString()


    }

    private fun customDialog(
        userCount: Int,
        userTeamName: String,
        question1: String,
        question2: String,
        question3: String,
        question4: String,
        question5: String,
        answer: String
    ) {

        val dialog = CustomDialog(this, userCount, userTeamName)

        try {

            dialog.setData(question1, question2, question3, question4, question5, answer)

        } catch (e: NullPointerException) {

            Toast.makeText(this, "노래를 재생시키고 켜주세요", Toast.LENGTH_LONG).show()

        }

        dialog.show()

    }

    override fun onDataPass(answerUserList: ArrayList<String>) {
        this.answerUserList.addAll(answerUserList)
        Log.d("answerUserList", answerUserList.toString())
    }


}