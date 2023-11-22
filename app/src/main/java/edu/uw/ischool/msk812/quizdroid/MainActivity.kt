package edu.uw.ischool.msk812.quizdroid

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
//import android.widget.Toolbar
import androidx.appcompat.widget.Toolbar


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

        // Use for extra credit step
        //descText.text = topics[position].shortDesc
        return view;
    }
    override fun getCount(): Int {
        Log.i("TopicAdapter", "count = ${super.getCount()}")
        return super.getCount()
    }
}

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Receiver", "Received Broadcast, starting service")
        val serviceIntent = Intent(context, DownloadService::class.java).apply {
            putExtra("url", intent?.getStringExtra("url"))
        }
        context?.startService(serviceIntent)    }
}

class MainActivity : AppCompatActivity() {
    lateinit var quizTopics: ListView
    lateinit var actionBar : Toolbar
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar = findViewById(R.id.toolbar)
        setSupportActionBar(actionBar)
        quizTopics = findViewById(R.id.listView)

        val internetStatus = checkForInternet(applicationContext)

        if(isAirplaneModeOn()) {
            showAirplaneModeDialog()
        } else if (!internetStatus) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
        } else {
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

    override fun onResume() {
        super.onResume()
        if(isAirplaneModeOn()){
            showAirplaneModeDialog()
        }
    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    private fun showAirplaneModeDialog() {
        Log.i("DownloadService", "Airplane mode is on. Ask user to turn it off.")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Airplane Mode On")
        alertDialogBuilder.setMessage("Airplane mode is currently on. Would you like to go to settings to turn airplane mode off?")
        alertDialogBuilder.setPositiveButton("Settings") { _: DialogInterface, _: Int -> this.startActivity(Intent(Settings.ACTION_SETTINGS)) }
        alertDialogBuilder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    /*=private fun checkForInternet() : Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.preferences -> {
                val intent = Intent(this, Preferences::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}