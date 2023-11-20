package com.enjoy_project.musicquiz

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class CustomDialog(context: Context, private val onDialogComplete: () -> Unit): Dialog(context) {

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

    private lateinit var itemClickListener: ItemClickListener

    private lateinit var question1: RadioButton
    private lateinit var question2: RadioButton
    private lateinit var question3: RadioButton
    private lateinit var question4: RadioButton
    private lateinit var question5: RadioButton

    private lateinit var btnNext: Button
    private lateinit var btnCancel: TextView
    private lateinit var btnComplete: TextView

    private lateinit var userColor: ImageView

    private var question1Text = ""
    private var question2Text = ""
    private var question3Text = ""
    private var question4Text = ""
    private var question5Text = ""

    private var selectedOption: String? = null
    private var userCount = 0
    private var totalUserCount = 8 // 원하는 횟수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        btnCancel = findViewById<TextView>(R.id.btnCancel)
        btnComplete = findViewById<TextView>(R.id.btnComplete)

        userColor = findViewById<ImageView>(R.id.userColor)

        btnNext = findViewById<Button>(R.id.btnNext)

        question1 = findViewById<RadioButton>(R.id.radioOption1)
        question2 = findViewById<RadioButton>(R.id.radioOption2)
        question3 = findViewById<RadioButton>(R.id.radioOption3)
        question4 = findViewById<RadioButton>(R.id.radioOption4)
        question5 = findViewById<RadioButton>(R.id.radioOption5)

        question1.text = question1Text
        question2.text = question2Text
        question3.text = question3Text
        question4.text = question4Text
        question5.text = question5Text

        setRadioClickListener()

        btnNext.setOnClickListener {
            handleNextButtonClick()
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

        tvCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기 (Close the dialog)
        }

        tvCall.setOnClickListener {
            // interface를 이용해 다이얼로그를 호출한 곳에 값을 전달함
            // Use interface to pass the value to the activty or fragment
            itemClickListener.onClick("031-467-0000")
            dismiss()
        }
    }

    // 사이즈를 조절하고 싶을 때 사용 (use it when you want to resize dialog)
    private fun resize(dialog: Dialog, width: Float, height: Float){
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
            userCount++
            if (userCount < totalUserCount) {
                userColor.setBackgroundResource(userColorArray[userCount])
                Toast.makeText(context, "Next clicked, Repeat count: $userCount", Toast.LENGTH_SHORT).show()
            } else {
                btnNext.isEnabled = false
                btnComplete.visibility = android.view.View.VISIBLE
                Toast.makeText(context, "Repeat count reached. Click Complete to finish.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please select an option.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCompleteButtonClick() {
        if (userCount == totalUserCount) {
            // 완료 버튼 클릭 시 다이얼로그 종료
            onDialogComplete.invoke()
            dismiss()
        }
    }

    // 왜 생성자에 안했냐? --> 재생 버튼을 눌러야 데이터를 받아오는데 안눌렀을 때 다이얼로그를 켜버리면 빈 생성자가 되어 데이터가 없잖아.
    // 아닌데? 별 차이 없겠네, 내가 동작에 따른 데이터를 넣어주는게 아니라 그냥 켰을 때 데이터가 들어가있어야 한다면 생성자에 넣는게 맞겠네.
    fun setData(question1: String,
                question2: String,
                question3: String,
                question4: String,
                question5: String) {

        this.question1Text = question1
        this.question2Text = question2
        this.question3Text = question3
        this.question4Text = question4
        this.question5Text = question5

    }
}