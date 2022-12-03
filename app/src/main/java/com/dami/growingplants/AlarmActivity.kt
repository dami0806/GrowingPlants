package com.dami.growingplants

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import com.dami.alarm.AlarmReceiver
import java.util.*

class AlarmActivity : AppCompatActivity() {
    companion object {
        const val TAG = "AlarmActivity"
    }
    lateinit var calBtn: ImageView
    lateinit var toDoBtn: ImageView
    lateinit var alarmBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager


        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        var onetimeAlarmToggle: ToggleButton

        var realtimePeriodicAlarmToggle: ToggleButton

        realtimePeriodicAlarmToggle= findViewById(R.id.realtimePeriodicAlarmToggle)
        onetimeAlarmToggle= findViewById(R.id.onetimeAlarmToggle)

        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
        alarmBtn = findViewById(R.id.AlarmBtn)

//30분뒤 알리기
        onetimeAlarmToggle.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val toastMessage = if (isChecked) {
                val triggerTime = (SystemClock.elapsedRealtime()
                        + 3 * 1000)
                alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
                "30분뒤 알림을 보내요!"
            } else {
                alarmManager.cancel(pendingIntent)
                "30분뒤 알림이 꺼졌어요"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        })
// realtimePeriodicAlarmToggle //6시마다 알리기
        realtimePeriodicAlarmToggle.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val toastMessage: String = if (isChecked) {
                val repeatInterval: Long = 24*60 * 60 * 1000 // 15min
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 18)
                    set(Calendar.MINUTE, 0)
                }

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    repeatInterval,
                    pendingIntent
                )
                "오후 6시마다 알림이 울려요"
            } else {
                alarmManager.cancel(pendingIntent)
                "알림이 꺼졌어요"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        })


        toDoBtn.setOnClickListener {
            var intent = Intent(this, TodoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }
        calBtn.setOnClickListener {
            calBtn.setOnClickListener {
                var intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }

        }
        alarmBtn.setOnClickListener {


        }
    }
}