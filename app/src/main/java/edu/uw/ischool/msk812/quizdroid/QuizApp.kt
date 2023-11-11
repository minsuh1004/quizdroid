package edu.uw.ischool.msk812.quizdroid

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader




data class Quiz(val text: String, val answer: Int, val answers: List<String>)
data class Topic (val title: String, val desc: String, val questions: List<Quiz>)

interface TopicRepository {
    fun getAllTopics() : List<Topic>
    fun getTopic(index: Int) : Topic
    fun getQuiz(topicNum: Int, index: Int) : Quiz
}

class MockTopicRepository : TopicRepository {

    val readFile: String = FileReader("/data/local/tmp/questions.json").readText()
    val fileData: List<Topic> = Gson().fromJson(readFile, object : TypeToken<List<Topic>>() {}.type)
    override fun getAllTopics(): List<Topic> {
        val topicsList : MutableList<Topic> = mutableListOf()
        for (i in fileData.indices) {
            topicsList.add(Topic(fileData[i].title, fileData[i].desc, fileData[i].questions))
        }
        return fileData
    }

    override fun getTopic(index: Int): Topic {
        return fileData[index]
    }

    override fun getQuiz(topicNum: Int, index: Int): Quiz {
        return fileData[topicNum].questions[index]
    }
}

class QuizApp : Application() {
    lateinit var topicRepository: TopicRepository

    companion object {
        private lateinit var instance: QuizApp

        fun getInstance(): QuizApp {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = QuizApp()
                }
                return instance
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "onCreate() loaded and running")
        instance = this

        topicRepository = MockTopicRepository()
    }
}
