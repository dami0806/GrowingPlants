package com.dami.growingplants

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_todo.*
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    lateinit var calBtn: ImageView
    lateinit var toDoBtn: ImageView
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    private lateinit var key: String
    private lateinit var keyL: String
    private lateinit var fbkey: String
    private lateinit var  keylist: ArrayList<String>
    private lateinit var Datetext: String
    private lateinit var listviewAdapter: ListViewAdapter4
    val list_itemText = arrayListOf<String>()
    var keyList = ArrayList<String>()
    var d:String?=null
    val list_item = mutableListOf<String>()
    val list_itemtext = mutableListOf<String>()
    var textList = arrayListOf<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)

        calendarView.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->

                Datetext =
                    (year.toString() + "년 " + (month + 1).toString() + "월 " + dayOfMonth.toString() + "일")

                // set this date in TextView for Display
                dateTV.setText(Datetext)
                Log.d("테텥ㄱ",getDateData(dateTV.text.toString()).toString()) //글의 날짜정보 가져옴
                getCommentData(dateTV.text.toString())

            })


        val listview = findViewById<ListView>(R.id.listView)
        listviewAdapter = ListViewAdapter4(list_item)
        listview.adapter = listviewAdapter

        add.setOnClickListener {
        addTask()}
        clear.setOnClickListener {
        clearBtn()}

        listview.setOnItemClickListener { parent, view, position, id ->



        }






        toDoBtn.setOnClickListener {
            var intent = Intent(this, TodoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            key = intent.putExtra("dateKey",dateTV.text.toString()).toString()


            startActivity(intent)

        }
        calBtn.setOnClickListener {

        }


    }
fun addTask(){

       list_itemText.clear()
         Log.d("비교", dateTV.text.toString())
         list_item.add(editText.text.toString())

         var et = editText.text.toString()
       Log.d("보기",et.toString())
         //FB에 넣기
         UserApiClient.instance.me { user, error ->
             fbkey = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
             FBRef.todoDate
                 .child(user!!.id.toString())
                 .child(dateTV.text.toString())
                 .push()
                 .setValue(ListViewModel(et,false))
             list_itemText.add(et)
         }

         editText.text.clear()

}
    fun deleteTask(){

        list_itemText.clear()

        //FB에 넣기
        UserApiClient.instance.me { user, error ->
            fbkey = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
            FBRef.todoDate
                .child(user!!.id.toString())
                .child(dateTV.text.toString())
                .removeValue()

        }



    }
    fun clearBtn(){
        list_item.clear()
        listviewAdapter.notifyDataSetChanged()
        UserApiClient.instance.me { user, error ->

            FBRef.todoDate
                .child(user!!.id.toString())
                .child(dateTV.text.toString())
                .removeValue()
        }
        getCommentData(dateTV.text.toString())
        listviewAdapter.notifyDataSetChanged()//어댑터 동기화
    }
fun getCommentData(date: String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
  val postListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
          list_item.clear()
          list_itemtext.clear()
          for (dataModel in dataSnapshot.children) {
              if (date == dataModel.key) {

                  for (i in dataModel.children) {
                      Log.d("확인list2", i.value.toString())
                      Log.d("확인list2", i.key.toString())
                      //val item = i.getValue(ListViewModel::class.java)
                      //  Log.d("확인인",i.getValue(ListViewModel::class.java).toString())
                      list_item.add(i.value.toString())
                      keyList.add(i.key.toString())
                      for (j in i.children) {

                          Log.d("확인list3", j.value.toString())
                          if(j.key.toString().contains("text=")){
                              list_itemtext.add(j.key.toString())
                          }
                      }

                  }}
              listviewAdapter.notifyDataSetChanged()//어댑터 동기화
          }
      }

      override fun onCancelled(databaseError: DatabaseError) {
      }
  }
  UserApiClient.instance.me { user, error ->
      FBRef.todoDate.child(user!!.id.toString())
          .addValueEventListener(postListener)
  }
}

fun getDateData(dateData:String): String? {

//comment 아래 board아래 commentkey 아래 comment 데이터들
  val postListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {

          for (dataModel in dataSnapshot.children) {
             d =  dataModel.key.toString()
              if(d==dateData){
              Log.d("테텥ㄱ1",d.toString()) //글의 날짜정보 가져옴

                  for(i in dataModel.children){

                  }
          }}
      }

      override fun onCancelled(databaseError: DatabaseError) {
      }
  }
  UserApiClient.instance.me { user, error ->
      FBRef.todoDate.child(user!!.id.toString())
          .addValueEventListener(postListener) }
return d
}
    fun TextData(dateTV:String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {


                    for (i in dataModel.children) {
                        //Log.d("담담",i.value.toString())
                        Log.d("담담", i.toString())
                        if (i.key == "text") {
                            Log.d("담담1", i.value.toString())
                            textList.add(i.value.toString())

                        }
                    }
                    Log.d("담담2", textList.toString())
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.todoDate
                .child(user!!.id.toString())
                .child(dateTV)
                .addValueEventListener(postListener)
        }

    }

}
