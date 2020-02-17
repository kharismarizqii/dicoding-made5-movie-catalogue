package com.kharismarizqii.moviecatalogueuiux

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.kharismarizqii.moviecatalogueuiux.MainActivity.Companion.repeatTime
import com.kharismarizqii.moviecatalogueuiux.model.ReminderModel
import com.kharismarizqii.moviecatalogueuiux.preference.ReminderPreference
import com.kharismarizqii.moviecatalogueuiux.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_settings_preference.*


class SettingsPreferenceActivity : AppCompatActivity() {

    private lateinit var reminderModel: ReminderModel
    private lateinit var alarmReceiver: AlarmReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_preference)

        val reminderPreference = ReminderPreference(this)
        if (reminderPreference.getReminder().isReminded){
            switch1.setChecked(true)
        } else{
            switch1.setChecked(false)
        }
        alarmReceiver = AlarmReceiver()

        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                saveReminder(true)

                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTime, "youuuu")

            } else {
                saveReminder(false)
                alarmReceiver.cancelAlarm(this)
            }
        })
    }

    private fun saveReminder(b: Boolean) {
        val reminderPreference = ReminderPreference(this)
        reminderModel = ReminderModel()

        reminderModel.isReminded = b

        reminderPreference.setReminder(reminderModel)
    }
}
