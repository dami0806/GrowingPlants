package com.dami.growingplants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_todo.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var calBtn:ImageView
    lateinit var toDoBtn:ImageView
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    private lateinit var key: String
    val list_item = mutableListOf<String>()
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

                            val Date = (year.toString() + "년" + (month + 1).toString()+"월"+dayOfMonth.toString())
                            // set this date in TextView for Display
                            dateTV.setText(Date)
                        })

        var itemlist = arrayListOf<String>()
        var adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_multiple_choice
            , list_item)

        val listview = findViewById<ListView>(R.id.listView)
        val listviewAdapter = ListViewAdapter4(list_item)
        listview.adapter = listviewAdapter


        // add버튼 클릭
        add.setOnClickListener {

            list_item.add(editText.text.toString())
            itemlist.add(editText.text.toString())
            //listView.adapter =  adapter

            listviewAdapter.notifyDataSetChanged()
            adapter.notifyDataSetChanged()

           var et= editText.text.toString()

            //FB에 넣기
            UserApiClient.instance.me { user, error ->

               key = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴

                FBRef.todoDate
                    .child(dateTV.text.toString())
                    .child(key)
                    .setValue(et)

            }

            editText.text.clear()
        }


        // Clear버튼 클릭
        clear.setOnClickListener {

            itemlist.clear()
            adapter.notifyDataSetChanged()
            FBRef.todoDate
                .child(dateTV.text.toString())
                .removeValue()

        }
        //
        listView.setOnItemClickListener { adapterView, view, i, l ->
            android.widget.Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), android.widget.Toast.LENGTH_SHORT).show()
        }
        // 삭제버튼 클릭
        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item))
                {
                    adapter.remove(itemlist.get(item))
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()

            FBRef.todoDate
                .child(dateTV.text.toString())
                .child(key)
                .removeValue()



        }

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