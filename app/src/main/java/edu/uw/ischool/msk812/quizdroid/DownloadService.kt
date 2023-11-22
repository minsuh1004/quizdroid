package edu.uw.ischool.msk812.quizdroid

import android.app.AlertDialog
import android.app.IntentService
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DownloadService : IntentService("DownloadService") {
    override fun onHandleIntent(intent: Intent?) {
        val sharedPref = getSharedPreferences("QuizDroidPref", MODE_PRIVATE)
        val url = sharedPref.getString("url", "http://tednewardsandbox.site44.com/questions.json")
        //val url = sharedPref.getString("url", "http://tednewardsandbox.site44.com/questions.json").toString()
        val fileName = "questions.json"
        try {
            val executor : Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                //val currentURL = URL("http", "tednewardsandbox.site44.com", 80, "/questions.json")
                val currentURL = URL(url)
                val urlConnection = currentURL.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.getInputStream()
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
                    onHandleIntent(null)
                }
                .setNegativeButton("No") {dialog, id ->
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            alert.show()
        }
    }
}