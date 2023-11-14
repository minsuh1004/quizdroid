package edu.uw.ischool.msk812.quizdroid

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.JsonReader
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.io.path.Path


data class Quiz(val text: String, val answer: Int, val answers: List<String>)
data class Topic (val title: String, val desc: String, val questions: List<Quiz>)

// Use for extra credit step
//data class Topic (val title: String, val shortDesc: String, val longDesc: String, val questions: List<Quiz>)

interface TopicRepository {
    fun getAllTopics() : List<Topic>
    fun getTopic(index: Int) : Topic
    fun getQuiz(topicNum: Int, index: Int) : Quiz
}

class MockTopicRepository(context: Context) : TopicRepository {
    // Must upload file into device
    val readFile = File(context.filesDir, "questions.json").readText()
    //val readFile: String = FileReader("/data/user/0/edu.uw.ischool.msk812.quizdroid/files/questions.json").readText()

    // Use for extra credit step
    //val readFile = File(context.filesDir, "msk812Questions.json").readText()

    val fileData: List<Topic> = Gson().fromJson(readFile, object : TypeToken<List<Topic>>() {}.type)
    override fun getAllTopics(): List<Topic> {
        val topicsList : MutableList<Topic> = mutableListOf()
        for (i in fileData.indices) {
            topicsList.add(Topic(fileData[i].title, fileData[i].desc, fileData[i].questions))
        }

        // Use for extra credit step
        /*for (i in fileData.indices) {
            topicsList.add(Topic(fileData[i].title, fileData[i].shortDesc, fileData[i].longDesc, fileData[i].questions))
        }*/
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
        Log.i("TopicRepo", "filesDir = $filesDir")

        topicRepository = MockTopicRepository(applicationContext)
    }
}
