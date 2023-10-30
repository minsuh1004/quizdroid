package edu.uw.ischool.msk812.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    lateinit var quizTopics: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizTopics = findViewById(R.id.listView)

        val topics: Array<String> = arrayOf("Math", "Physics", "Marvel Super Heroes")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
            android.R.id.text1, topics)
        quizTopics.adapter = adapter

        quizTopics.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, TopicOverview::class.java)
            intent.putExtra("topic", topics[position])
            startActivity(intent)
        }
    }
}