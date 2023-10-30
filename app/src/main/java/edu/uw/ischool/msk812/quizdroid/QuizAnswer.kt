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

        val bundle3 = intent.getBundleExtra("bundle") as Bundle
        val currentQuestion = bundle3.getInt("currentQuestion")
        val totalQuestions = intent.getIntExtra("totalQuestions", 1)
        val userAnswer = intent.getStringExtra("userAnswer")
        val correctAnswer = bundle3.getString("correct")
        val topic = intent.getStringExtra("topic")


        chosenText.text = "$userAnswer"
        correctText.text = "$correctAnswer"

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