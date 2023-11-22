package edu.uw.ischool.msk812.quizdroid

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.JsonReader
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
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
    //var readFile: String = ""
    var fileData: MutableList<Topic> = mutableListOf()
    //val readFile = File(context.filesDir, "questions.json").readText()
    //val readFile: String = FileReader("/data/user/0/edu.uw.ischool.msk812.quizdroid/files/questions.json").readText()

    //val fileData: List<Topic> = Gson().fromJson(readFile, object : TypeToken<List<Topic>>() {}.type)
    init {
        fetchJSONData(context)
    }

    private fun fetchJSONData(context: Context) {
        //val file = File(Environment.getExternalStorageDirectory().path, "questions.json")
        //val externalFileDir = context.getExternalFilesDir("QuizDroidRepo")
        //Log.i("FileReader", "Files Dir is $externalFileDir")

        val file = context.filesDir
        Log.i("FileReader", "Files Dir is $file")
        //val file = File(context.filesDir.path, "questions.json")
        try {
            val reader = FileReader("$file/questions.json")
            val data = JSONArray(reader.readText())
            Log.i("JSONArray", "JSON content is $data")
            reader.close()
            for (tIndex in 0 until data.length()) {
                val topicObj = data.getJSONObject(tIndex)
                val questions = topicObj.getJSONArray("questions")
                val questionList = arrayListOf<Quiz>()
                for (qIndex in 0 until questions.length()) {
                    val quiz = questions.getJSONObject(qIndex)
                    val answerChoice = quiz.getJSONArray("answers")
                    val answers = arrayListOf<String>()
                    for (aIndex in 0..3) {
                        answers.add(answerChoice[aIndex].toString())
                    }
                    questionList.add(
                        Quiz(
                            quiz.getString("text"),
                            (quiz.getInt("answer") - 1),
                            answers
                        )
                    )
                }
                fileData.add(
                    Topic(
                        topicObj.getString("title"),
                        topicObj.getString("desc"),
                        questionList
                    )
                )
            }
        } catch (e: IOException) {
            Log.e("MockTopicRepository", "Error reading file", e)
            e.printStackTrace()
        }
    }
    override fun getAllTopics(): List<Topic> {
        /*val topicsList : MutableList<Topic> = mutableListOf()
        for (i in fileData.indices) {
            topicsList.add(Topic(fileData[i].title, fileData[i].desc, fileData[i].questions))
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
        runBlocking { downloadFile() }

        topicRepository = MockTopicRepository(applicationContext)
    }

    fun downloadFile() {
        try {
            val executor : Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val currentURL = URL("http", "tednewardsandbox.site44.com", 80, "/questions.json")
                val fileName = "questions.json"
                //val currentURL = URL(url)
                val urlConnection = currentURL.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream
                    val reader = InputStreamReader(inputStream)
                    Log.i("Download", "Downloading and writing JSON")
                    //val downloadFolder = getExternalFilesDir("QuizDroid")
                    val downloadFolder = filesDir
                    Log.i("FilePath", "$downloadFolder")
                    val file = File(downloadFolder, fileName)
                    val outputStream = FileOutputStream(file)
                    outputStream.write(reader.readText().toByteArray())
                    reader.close()
                    outputStream.close()
                }
            }
        } catch (e: Exception) {
            Log.e("DownloadService", "Download failed", e)
            val alert = AlertDialog.Builder(this)
                .setTitle("Download Error")
                .setMessage("Download has failed. Would you like to retry or quit the app?")
                .setPositiveButton("Yes") {dialog, id ->
                    downloadFile()
                }
                .setNegativeButton("No") {dialog, id ->
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            alert.show()
        }
    }
}
