package edu.uw.ischool.msk812.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible

class QuizQuestion : AppCompatActivity() {
    class Question (val q: String, val a1: String, val a2: String, val a3: String, val a4: String, val correctAnswer: Int) {
        fun getCorrect() : String {
            return when (correctAnswer) {
                1 -> a1
                2 -> a2
                3 -> a3
                4 -> a4
                else -> ""
            }
        }
    }

    private val mathQuestions: Array<Question> = arrayOf(
        Question("What is 2 + 2?", "3", "4", "2", "7", 2),
        Question("What is 13 * 29?", "377", "437", "378", "245", 1),
        Question("What is the square root of 144?", "8", "16", "13", "12", 4)
    )

    private val physicsQuestions: Array<Question> = arrayOf(
        Question("What is the speed in combination of the direction of an object's motion?", "Pressure", "Watt", "Velocity", "Acceleration", 3),
        Question("Which subatomic particle has neutral charge?", "Proton", "Antineutron", "Electron", "Neutron", 4),
        Question("What is the equation for measuring pressure?", "P = x/t", "P = F/A", "", "", 2)
    )

    private val marvelQuestions: Array<Question> = arrayOf(
        Question("What is Spider-Man's secret identity?", "Ben Parker", "Ben Reilly", "Peter Parker", "Pater Pan", 3),
        Question("Which superhero fights mainly with a shield?", "Captain America", "Iron Man", "Captain Marvel", "Star-Lord", 1),
        Question("Who lead the organization S.H.I.E.L.D.?", "Black Widow", "Nick Fury", "Charles Xavier", "Dr. Strange", 2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        val questionNum = findViewById<TextView>(R.id.questionNum)
        val questionText = findViewById<TextView>(R.id.questionText)
        val answerChoices = findViewById<RadioGroup>(R.id.radioGroup)
        val answer1 = findViewById<TextView>(R.id.answer1)
        val answer2 = findViewById<TextView>(R.id.answer2)
        val answer3 = findViewById<TextView>(R.id.answer3)
        val answer4 = findViewById<TextView>(R.id.answer4)
        val submitBtn = findViewById<Button>(R.id.submitButton)

        val bundle : Bundle = intent.getBundleExtra("bundle") as Bundle
        val topic = intent.getStringExtra("topic")
        val currentQuestion = bundle.getInt("currentQuestion")
        val totalQuestions = intent.getIntExtra("totalQuestions", 1)

        val qIndex : Int = currentQuestion - 1
        submitBtn.isVisible = false

        fun topicQuestions(name: String?): String {
            return when (name) {
                "Math" -> mathQuestions[qIndex].q
                "Physics" -> physicsQuestions[qIndex].q
                "Marvel Super Heroes" -> marvelQuestions[qIndex].q
                else -> ""
            }
        }

        fun choices1(name: String?): String {
            return when (name) {
                "Math" -> mathQuestions[qIndex].a1
                "Physics" -> physicsQuestions[qIndex].a1
                "Marvel Super Heroes" -> marvelQuestions[qIndex].a1
                else -> ""
            }
        }

        fun choices2(name: String?): String {
            return when (name) {
                "Math" -> mathQuestions[qIndex].a2
                "Physics" -> physicsQuestions[qIndex].a2
                "Marvel Super Heroes" -> marvelQuestions[qIndex].a2
                else -> ""
            }
        }

        fun choices3(name: String?): String {
            return when (name) {
                "Math" -> mathQuestions[qIndex].a3
                "Physics" -> physicsQuestions[qIndex].a3
                "Marvel Super Heroes" -> marvelQuestions[qIndex].a3
                else -> ""
            }
        }

        fun choices4(name: String?): String {
            return when (name) {
                "Math" -> mathQuestions[qIndex].a4
                "Physics" -> physicsQuestions[qIndex].a4
                "Marvel Super Heroes" -> marvelQuestions[qIndex].a4
                else -> ""
            }
        }

        fun correctChoice(name: String?): String {
            return when (name) {
                "Math" -> mathQuestions[qIndex].getCorrect()
                "Physics" -> physicsQuestions[qIndex].getCorrect()
                "Marvel Super Heroes" -> marvelQuestions[qIndex].getCorrect()
                else -> ""
            }
        }

        questionNum.text = "Question $currentQuestion"
        questionText.text = topicQuestions(topic)
        answer1.text = choices1(topic)
        answer2.text = choices2(topic)
        answer3.text = choices3(topic)
        answer4.text = choices4(topic)
        val chosenTopic = correctChoice(topic)

        answerChoices.setOnCheckedChangeListener { _, _ ->
            submitBtn.isVisible = true
        }

        submitBtn.setOnClickListener {
            val userAnswer : RadioButton = findViewById(answerChoices.checkedRadioButtonId)
            val bundle2 = Bundle()
            bundle2.putString("correct", chosenTopic)
            bundle2.putInt("score", bundle.getInt("score"))
            bundle2.putInt("currentQuestion", currentQuestion)

            val intent = Intent(this, QuizAnswer::class.java)
            intent.putExtra("topic", topic)
            intent.putExtra("totalQuestions", totalQuestions)
            intent.putExtra("userAnswer", userAnswer.text.toString())
            intent.putExtra("bundle", bundle2)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val miniBundle : Bundle = intent.getBundleExtra("bundle") as Bundle
        val currentQuestion = miniBundle.getInt("currentQuestion")
        val totalQuestions = intent.getIntExtra("totalQuestions", 1)
        val topic = intent.getStringExtra("topic")

        if (currentQuestion == 1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {

            val miniBundle2 = Bundle()
            miniBundle2.putInt("score", miniBundle.getInt("score"))
            miniBundle2.putInt("currentQuestion", currentQuestion - 1)

            val intent = Intent(this, QuizQuestion::class.java)
            intent.putExtra("topic", topic)
            intent.putExtra("totalQuestions", totalQuestions)
            intent.putExtra("bundle", miniBundle2)
            startActivity(intent)
        }
    }
}