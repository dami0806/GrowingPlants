package com.dami.growingplants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.core.view.get
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_todo.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var calBtn:ImageView
    lateinit var toDoBtn:ImageView
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    private lateinit var key: String

private lateinit var listviewAdapter:ListViewAdapter4
    val list_item = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
                dateTV = findViewById(R.id.idTVDate)
                calendarView = findViewById(R.id.calendarView)
                calendarView.setOnDateChangeListener(
                        OnDateChangeListener { view, year, month, dayOfMonth ->

                            val Date = (year.toString() + "년 " + (month + 1).toString()+"월 "+dayOfMonth.toString()+"일")

                            // set this date in TextView for Display
                            dateTV.setText(Date)
                        })



        val listview = findViewById<ListView>(R.id.listView)
        listviewAdapter = ListViewAdapter4(list_item)
        listview.adapter = listviewAdapter

           // listView.adapter =  adapter
           // adapter.notifyDataSetChanged()


       // getCommentData()
       // getCommentData(key)
        // add버튼 클릭
        getCommentData()
        add.setOnClickListener {

            list_item.add(editText.text.toString())

            var et= editText.text.toString()
            //FB에 넣기
            UserApiClient.instance.me { user, error ->

               key = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴

                FBRef.todoDate
                    .child(user!!.id.toString())
                    .child(dateTV.text.toString())
                    .push()
                    .setValue(ListViewModel(et))

            }

            editText.text.clear()
        }


            delete.setOnClickListener {

                val position: SparseBooleanArray = listView.checkedItemPositions
                val count = listView.count
                var item = count - 1
                while (item >= 0) {
                    if (position.get(item)) {
                        // adapter.remove(itemlist.get(item))
                    }
                    item--

                    UserApiClient.instance.me { user, error ->
                        // adapter.notifyDataSetChanged()
                        FBRef.todoDate
                            .child(user!!.id.toString())
                            .child(dateTV.text.toString())
                            .removeValue()
                    }

                }

        }
        // Clear버튼 클릭
        clear.setOnClickListener {

            UserApiClient.instance.me { user, error ->
           // adapter.notifyDataSetChanged()
            FBRef.todoDate
                .child(user!!.id.toString())
                .child(dateTV.text.toString())
                .removeValue()

        }
        }
        //
     /*   listView.setOnItemClickListener { adapterView, view, i, l ->
            android.widget.Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), android.widget.Toast.LENGTH_SHORT).show()*/

        /*// 삭제버튼 클릭
        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item))
                {
                   // adapter.remove(itemlist.get(item))
                }
                item--
            }
            position.clear()
           // adapter.notifyDataSetChanged()

            FBRef.todoDate
                .child(dateTV.text.toString())
                .child(key)
                .removeValue()



        }*/

        //Tap
        calBtn.setOnClickListener {

        }
        toDoBtn.setOnClickListener {
            var intent = Intent(this,TodoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }



    }
    fun getCommentData() {
//comment 아래 board아래 commentkey 아래 comment 데이터들
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_item.clear()
                for (dataModel in dataSnapshot.children) {

                    list_item.add(dataModel.value.toString())
                }
                listviewAdapter.notifyDataSetChanged()//어댑터 동기화
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.todoDate.child(user!!.id.toString())
                .child(dateTV.text.toString())
                .addValueEventListener(postListener)
        }
    }

}