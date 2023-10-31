package edu.uw.ischool.msk812.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverview : AppCompatActivity() {
    lateinit var beginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val topicName = findViewById<TextView>(R.id.topicName)
        val numQuestions = findViewById<TextView>(R.id.numQuestions)
        val topicDescription = findViewById<TextView>(R.id.description)
        beginButton = findViewById(R.id.beginButton)

        val topic = intent.getStringExtra("topic")
        val numOfQs = 3
        val numQuestionText = "There are $numOfQs question in this topic."
        val descDetails = "This quiz covers questions about $topic. Be sure to prepare yourself."

        topicName.text = topic
        numQuestions.text = numQuestionText

        val bundle = Bundle()
        bundle.putString("correct", "")
        bundle.putInt("score", 0)
        bundle.putInt("currentQuestion", 1)

        fun topicDesc(name: String?): String {
            return when (name) {
                "Math" -> ("$descDetails It will contain basic mathematical problems that you need to figure out.")
                "Physics" -> ("$descDetails It will ask about some physics terms and formulas that you need to see which answer fits the question.")
                "Marvel Super Heroes" -> ("$descDetails It will ask about some questions based on the history/events of Marvel heroes.")
                else -> ""
            }
        }
        topicDescription.text = topicDesc(topic)

        beginButton.setOnClickListener {
            val intent = Intent(this, QuizQuestion::class.java)
            intent.putExtra("topic", topic)
            intent.putExtra("totalQuestions", numOfQs)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }
    }
}