package edu.uw.ischool.msk812.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class QuizAnswer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_answer)

        val chosenText: TextView = findViewById(R.id.chosenAnswer)
        val correctText: TextView = findViewById(R.id.correctAnswer)
        val score: TextView = findViewById(R.id.scoreText)
        val button: Button = findViewById(R.id.nxtFinButton)


        val quizApp = (application as QuizApp)
        val repository = quizApp.topicRepository

        val bundle3 = intent.getBundleExtra("bundle") as Bundle
        val currentQuestion = bundle3.getInt("currentQuestion")
        val totalQuestions = intent.getIntExtra("totalQuestions", 1)
        val userAnswer = intent.getStringExtra("userAnswer")
        val correctAnswer = bundle3.getInt("correct")
        val topic = intent.getIntExtra("topic", 0)
        val qIndex = intent.getIntExtra("qIndex", 0)

        val quiz = repository.getQuiz(topic, qIndex)

        val checkCorrect =  when (correctAnswer) {
            1 -> quiz.answers[0]
            2 -> quiz.answers[1]
            3 -> quiz.answers[2]
            4 -> quiz.answers[3]
            else -> ""
        }

        chosenText.text = "$userAnswer"
        correctText.text = checkCorrect

        val currentScore = if (chosenText.text == correctText.text) {
            bundle3.getInt("score") + 1
        } else {
            bundle3.getInt("score")
        }

        score.text = "You have $currentScore out of $totalQuestions correct"

        if (currentQuestion == totalQuestions) {
            button.text = "Finish"
        } else {
            button.text = "Next"
        }

        button.setOnClickListener {
            if (button.text == "Next") {
                val bundle4 = Bundle()
                bundle4.putInt("score", 0)
                bundle4.putInt("currentQuestion", currentQuestion + 1)
                bundle4.putInt("score", currentScore)

                val intent = Intent(this, QuizQuestion::class.java)
                intent.putExtra("topic", topic)
                intent.putExtra("totalQuestions", totalQuestions)
                intent.putExtra("bundle", bundle4)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}