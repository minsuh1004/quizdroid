package edu.uw.ischool.msk812.quizdroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
//import android.widget.Toolbar
import androidx.appcompat.widget.Toolbar

class Preferences : AppCompatActivity() {
    lateinit var prefURL : EditText
    lateinit var prefTime : EditText
    lateinit var saveBtn : Button
    lateinit var actionBar : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        actionBar = findViewById(R.id.prefToolbar)
        setSupportActionBar(actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        prefURL = findViewById(R.id.prefURL)
        prefTime = findViewById(R.id.prefTime)
        saveBtn = findViewById(R.id.saveButton)

        val sharedPref = getSharedPreferences("QuizDroidPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        //val prefs = this.getPreferences(Context.MODE_PRIVATE)

        prefURL.setText(sharedPref.getString("url", "http://tednewardsandbox.site44.com/questions.json"))
        prefTime.setText(sharedPref.getInt("time", 0).toString())

        saveBtn.setOnClickListener {
            editor.putString("url", prefURL.text.toString())
            editor.putInt("time", prefTime.text.toString().toInt())
            editor.commit()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }
}