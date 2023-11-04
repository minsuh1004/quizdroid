package edu.uw.ischool.msk812.quizdroid

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView


class TopicAdapter(context: Context, private val topics : List<Topic>)
    : ArrayAdapter<Topic>(context, android.R.layout.simple_list_item_2, android.R.id.text1, topics) {
    init {
        Log.v("TopicAdapter", "Constructing")
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.i("TopicAdapter", "getView() called")
        val view = super.getView(position, convertView, parent);

        val titleText : TextView = view.findViewById(android.R.id.text1);
        val descText : TextView = view.findViewById(android.R.id.text2);
        Log.i("TopicAdapter", "text1 = ${titleText}, text2 = ${descText}")

        Log.i("TopicAdapter", "topics[position] = ${topics[position]}")
        titleText.text = topics[position].title
        descText.text = topics[position].shortDesc
        return view;
    }
    override fun getCount(): Int {
        Log.i("TopicAdapter", "count = ${super.getCount()}")
        return super.getCount()
    }
}

class MainActivity : AppCompatActivity() {
    lateinit var quizTopics: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizTopics = findViewById(R.id.listView)

        val quizApp = (application as QuizApp)
        val repository = quizApp.topicRepository
        val topicData = repository.getAllTopics()

        quizTopics.adapter = TopicAdapter(this, topicData)

        quizTopics.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, TopicOverview::class.java)
            intent.putExtra("topic", position)
            startActivity(intent)
        }
    }
}