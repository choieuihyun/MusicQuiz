package com.enjoy_project.musicquiz

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class CustomDialog(
    context: Context,
    private val totalUserCount: Int,
    private val userTeamName: String
) : Dialog(context) {

    interface OnDataPassDialogToActivityListener {
        fun onDataPass(answerUserList: ArrayList<String>)
    }

    private val userColorArray = arrayOf(
        R.color.red,
        R.color.blue,
        R.color.green,
        R.color.yellow,
        R.color.purple,
        R.color.black,
        R.color.orange,
        R.color.brown
    )

    private val retrofit = RetrofitImpl()

    private lateinit var itemClickListener: ItemClickListener

    private lateinit var radioGroup: RadioGroup
    private lateinit var question1: RadioButton
    private lateinit var question2: RadioButton
    private lateinit var question3: RadioButton
    private lateinit var question4: RadioButton
    private lateinit var question5: RadioButton

    private lateinit var btnNext: Button
    private lateinit var btnCancel: TextView
    private lateinit var btnComplete: TextView

    private lateinit var userColor: ImageView

    private var userList: List<String> = arrayListOf()
    private var answerUserList: ArrayList<String> = arrayListOf()

    private var question1Text = ""
    private var question2Text = ""
    private var question3Text = ""
    private var question4Text = ""
    private var question5Text = ""
    private var answer = ""

    private var selectedOption: String? = null
    private var userCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        CoroutineScope(Dispatchers.IO).launch {

            retrofit.getUserListByTeam(userTeamName) { userList ->

                if (userList != null) {

                    this@CustomDialog.userList = userList
                    Log.d("userTeam", userList.toString())
                }

            }
        }

        btnCancel = findViewById<TextView>(R.id.btnCancel)
        btnComplete = findViewById<TextView>(R.id.btnComplete)

        userColor = findViewById<ImageView>(R.id.userColor)

        btnNext = findViewById<Button>(R.id.btnNext)

        question1 = findViewById<RadioButton>(R.id.radioOption1)
        question2 = findViewById<RadioButton>(R.id.radioOption2)
        question3 = findViewById<RadioButton>(R.id.radioOption3)
        question4 = findViewById<RadioButton>(R.id.radioOption4)
        question5 = findViewById<RadioButton>(R.id.radioOption5)

        radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        question1.text = question1Text
        question2.text = question2Text
        question3.text = question3Text
        question4.text = question4Text
        question5.text = question5Text

        setRadioClickListener()

        btnNext.setOnClickListener {

            val selectedRadioButtonId = radioGroup.checkedRadioButtonId

            if (selectedRadioButtonId == -1) {
                Toast.makeText(context, "정답을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // 아무 작업도 수행하지 않고 함수 종료
            }

            handleNextButtonClick()
            radioGroup.clearCheck()
        }

        btnComplete.setOnClickListener {
            handleCompleteButtonClick()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        // 사이즈를 조절하고 싶을 때 사용 (use it when you want to resize dialog)
        // resize(this, 0.8f, 0.4f)

        // 배경을 투명하게 (Make the background transparent)
        // 다이얼로그를 둥글게 표현하기 위해 필요 (Required to round corner)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 바깥쪽 클릭시 종료되도록 함 (Cancel the dialog when you touch outside)
        setCanceledOnTouchOutside(true)

        // 취소 가능 유무
        setCancelable(true)

    }

    // 사이즈를 조절하고 싶을 때 사용 (use it when you want to resize dialog)
    private fun resize(dialog: Dialog, width: Float, height: Float) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val size = Point()
            windowManager.defaultDisplay.getSize(size)

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            dialog.window?.setLayout(x, y)
        } else {
            val rect = windowManager.currentWindowMetrics.bounds

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()
            dialog.window?.setLayout(x, y)
        }
    }

    interface ItemClickListener {
        fun onClick(message: String)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    private fun setRadioClickListener() {

        question1.setOnClickListener {
            selectedOption = question1Text
        }

        question2.setOnClickListener {
            selectedOption = question2Text
        }

        question3.setOnClickListener {
            selectedOption = question3Text
        }

        question4.setOnClickListener {
            selectedOption = question4Text
        }

        question5.setOnClickListener {
            selectedOption = question5Text
        }

    }

    private fun handleNextButtonClick() {

        if (selectedOption != null) {
            val currentUserCount = userCount++

            Log.d("sdfsdfop", selectedOption!!.substring(3))
            Log.d("sdfsdfans", answer.toString())

            if (userCount < totalUserCount) {

                userColor.setBackgroundResource(userColorArray[userCount])

                    // 문자열 자른이유는 1.이 번호랑 공백 지우기 위함.
                    if (selectedOption!!.substring(3) == answer) {

                        CoroutineScope(Dispatchers.IO).launch {

                            // userCount++가 먼저 실행되어서 0번째 인덱스에 추가하려는데 1번째 인덱스에 추가되어서 이렇게 해봤음
                            // 근데 순서를 제어하는게 아니라 이렇게 코드 단에서 인덱스로 제어하는게 올바른 구조인가. 이것이 문제로다.
                            retrofit.addUserCount(userList[currentUserCount], userTeamName)
                            answerUserList.add(userList[currentUserCount])
                            Log.d("dialogAnswerUserList", answerUserList.toString())

                        }

                    }

                    Toast.makeText(
                        context,
                        "Next clicked, Repeat count: $currentUserCount",
                        Toast.LENGTH_SHORT
                    ).show()

            } else {
                btnNext.isEnabled = false
                btnComplete.visibility = android.view.View.VISIBLE
                Toast.makeText(
                    context,
                    "Repeat count reached. Click Complete to finish.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(context, "Please select an option.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCompleteButtonClick() {
        if (userCount == totalUserCount) {
            // 완료 버튼 클릭 시 다이얼로그 종료, 마지막 인덱스 인원 count 추가
            if (selectedOption!!.substring(3) == answer) {

                CoroutineScope(Dispatchers.IO).launch {

                    // userCount++가 먼저 실행되어서 0번째 인덱스에 추가하려는데 1번째 인덱스에 추가되어서 이렇게 해봤음
                    // 근데 순서를 제어하는게 아니라 이렇게 코드 단에서 인덱스로 제어하는게 올바른 구조인가. 이것이 문제로다.
                    retrofit.addUserCount(userList[userCount-1], userTeamName)
                    answerUserList.add(userList[userCount-1])
                    onDataPassListener.onDataPass(answerUserList)
                    Log.d("answerUserListDialog", answerUserList.toString())
                    Log.d("userListCount", userList[userCount-1])

                }

            } else {

                CoroutineScope(Dispatchers.IO).launch {

                    // if문을 잘 봤으면 이걸로 고생 안했다..
                    onDataPassListener.onDataPass(answerUserList)

                }

            }
            //onDialogComplete.invoke()
            dismiss()
        }
    }


    // 왜 생성자에 안했냐? --> 재생 버튼을 눌러야 데이터를 받아오는데 안눌렀을 때 다이얼로그를 켜버리면 빈 생성자가 되어 데이터가 없잖아.
    // 아닌데? 별 차이 없겠네, 내가 동작에 따른 데이터를 넣어주는게 아니라 그냥 켰을 때 데이터가 들어가있어야 한다면 생성자에 넣는게 맞겠네.
    fun setData(
        question1: String,
        question2: String,
        question3: String,
        question4: String,
        question5: String,
        answer: String
    ) {

        this.question1Text = question1
        this.question2Text = question2
        this.question3Text = question3
        this.question4Text = question4
        this.question5Text = question5
        this.answer = answer

    }
}