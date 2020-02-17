package com.kharismarizqii.moviecatalogueuiux

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kharismarizqii.moviecatalogueuiux.model.ReminderModel
import com.kharismarizqii.moviecatalogueuiux.preference.ReminderPreference
import com.kharismarizqii.moviecatalogueuiux.preference.ReminderPreference.Companion
import com.kharismarizqii.moviecatalogueuiux.receiver.AlarmReceiver
import com.kharismarizqii.moviecatalogueuiux.receiver.AlarmReceiver.Companion.TYPE_REPEATING

class MainActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        const val repeatTime = "11:09"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            //this is belong to menu item, not the navigation
            R.id.navigation_movie, R.id.navigation_tv, R.id.navigation_fav
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        alarmReceiver = AlarmReceiver()
        val reminderPreference = ReminderPreference(this)
        if (reminderPreference.getReminder().isReminded){
            alarmReceiver.setRepeatingAlarm(this, TYPE_REPEATING, repeatTime, "youuuu")
        } else{
            alarmReceiver.cancelAlarm(this)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_change_settings ->{
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.reminder_settings ->{
                val intent = Intent(this@MainActivity, SettingsPreferenceActivity::class.java)
                startActivity(intent)

            }
        }

        return super.onOptionsItemSelected(item)
    }


}
