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

        val quizApp = (application as QuizApp)
        val repository = quizApp.topicRepository

        val bundle : Bundle = intent.getBundleExtra("bundle") as Bundle
        val topic = intent.getIntExtra("topic", 0)
        val currentQuestion = bundle.getInt("currentQuestion")
        val totalQuestions = intent.getIntExtra("totalQuestions", 1)

        val qIndex : Int = currentQuestion - 1
        val quiz = repository.getQuiz(topic, qIndex)
        submitBtn.isVisible = false

        questionNum.text = "Question $currentQuestion"
        questionText.text = quiz.text
        answer1.text = quiz.answers[0]
        answer2.text = quiz.answers[1]
        answer3.text = quiz.answers[2]
        answer4.text = quiz.answers[3]
        val correct = quiz.answer

        answerChoices.setOnCheckedChangeListener { _, _ ->
            submitBtn.isVisible = true
        }

        submitBtn.setOnClickListener {
            val userAnswer : RadioButton = findViewById(answerChoices.checkedRadioButtonId)
            val bundle2 = Bundle()
            bundle2.putInt("correct", correct)
            bundle2.putInt("score", bundle.getInt("score"))
            bundle2.putInt("currentQuestion", currentQuestion)

            val intent = Intent(this, QuizAnswer::class.java)
            intent.putExtra("topic", topic)
            intent.putExtra("qIndex", qIndex)
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
        val topic = intent.getIntExtra("topic", 0)

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