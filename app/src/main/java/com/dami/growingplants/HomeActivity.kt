package com.dami.growingplants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView


class HomeActivity : AppCompatActivity() {
    lateinit var calBtn:ImageView
    lateinit var toDoBtn:ImageView
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)


                dateTV = findViewById(R.id.idTVDate)
                calendarView = findViewById(R.id.calendarView)


                calendarView
                    .setOnDateChangeListener(
                        OnDateChangeListener { view, year, month, dayOfMonth ->

                            val Date = (dayOfMonth.toString() + "-"
                                    + (month + 1) + "-" + year)

                            // set this date in TextView for Display
                            dateTV.setText(Date)
                        })



        //Tap
        calBtn.setOnClickListener {

        }
        toDoBtn.setOnClickListener {
            var intent = Intent(this,TodoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }



    }
}