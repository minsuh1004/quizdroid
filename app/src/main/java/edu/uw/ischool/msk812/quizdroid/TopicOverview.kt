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

        val quizApp = (application as QuizApp)
        val repository = quizApp.topicRepository

        val topic = intent.getIntExtra("topic", 0)
        val topicInfo = repository.getTopic(topic)

        val numOfQs = topicInfo.questions.size
        val numQuestionText = if (topicInfo.questions.size == 1) {
            "There is $numOfQs question in this topic."
        } else {
            "There are $numOfQs questions in this topic."
        }

        topicName.text = topicInfo.title
        numQuestions.text = numQuestionText
        topicDescription.text = topicInfo.desc

        // Use for extra credit step
        //topicDescription.text = topicInfo.longDesc

        val bundle = Bundle()
        bundle.putString("correct", "")
        bundle.putInt("score", 0)
        bundle.putInt("currentQuestion", 1)

        beginButton.setOnClickListener {
            val intent = Intent(this, QuizQuestion::class.java)
            intent.putExtra("topic", topic)
            intent.putExtra("totalQuestions", numOfQs)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }
    }
}