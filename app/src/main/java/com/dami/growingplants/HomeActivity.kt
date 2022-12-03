package com.dami.growingplants

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.listview_item.*
import java.time.format.DateTimeFormatter
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var calBtn: ImageView
    lateinit var toDoBtn: ImageView
    lateinit var alarmBtn: ImageView
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    private lateinit var key: String
    private lateinit var fbkey: String
    private lateinit var Datetext: String
    private lateinit var listviewAdapter: ListViewAdapter4
    val list_itemText = arrayListOf<String>()
    var keyList = ArrayList<String>()
    val list_item = mutableListOf<ListViewModel>()
    val list_itemtext = mutableListOf<String>()
    var textList = arrayListOf<String>()
    lateinit var dayOfMonthString:String
    lateinit var add: ImageView
    lateinit var clear: ImageView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
        alarmBtn = findViewById(R.id.AlarmBtn)

        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)
        add = findViewById(R.id.add)
        clear = findViewById(R.id.clear)
        clear.setOnClickListener {
            clearBtn()
        }
        calendarView.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->
                dayOfMonthString = dayOfMonth.toString()
                if(dayOfMonth<10){
                    dayOfMonthString = "0"+ dayOfMonth.toString()
                }

                Datetext =
                    (year.toString() + "년 " + (month + 1).toString() + "월 " + dayOfMonthString + "일")

                // set this date in TextView for Display
                dateTV.setText(Datetext)
                Log.d("나오는수",dayOfMonthString)
                Log.d("나오는수",dayOfMonth.toString())
                getCommentData(dateTV.text.toString())

            })

        val listview = findViewById<ListView>(R.id.listView)
        listviewAdapter = ListViewAdapter4(list_item)
        listview.adapter = listviewAdapter

       add.setOnClickListener {
        addTask()}


        listview.setOnItemLongClickListener { parent, view, position, id ->
           removeList(position)
            true
        }


        toDoBtn.setOnClickListener {
            var intent = Intent(this, TodoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            key = intent.putExtra("dateKey",dateTV.text.toString()).toString()


            startActivity(intent)

        }
        calBtn.setOnClickListener {


        }
        alarmBtn.setOnClickListener {
            var intent = Intent(this, AlarmActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }

    }
    private fun removeList(position: Int) {
        var dlg = AlertDialog.Builder(this)
        dlg.setTitle("growingPlant")
        dlg.setMessage("이 일정을 삭제하시겠습니까?")
        dlg.setIcon(R.drawable.img_2)

        dlg.setNegativeButton("삭제") { dialog, which ->


            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataModel in dataSnapshot.children) { //datModel: key값

                        var selectitem=textList[position]
                        Log.d("selectitem",selectitem.toString())
                        Log.d("selectitem",dataModel.getValue(ListViewModel::class.java)!!.text.toString())
                        if (selectitem == dataModel.getValue(ListViewModel::class.java)!!.text.toString()) //클릭한 list[postiion]time과 dateModel의 value time 같을시 삭제
                        {
                            Log.d("selectitem1",dataModel.key.toString())
                            UserApiClient.instance.me { user, error ->
                                FBRef.todoDate
                                    .child(user!!.id.toString())
                                    .child(dateTV.text.toString())
                                    .child(dataModel.key.toString())
                                    .removeValue()
                            }

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(
                        "ContentListActivity",
                        "loadPost:onCancelled",
                        databaseError.toException()
                    )
                }
            }

            UserApiClient.instance.me { user, error ->
                fbkey = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
                FBRef.todoDate
                    .child(user!!.id.toString())
                    .child(dateTV.text.toString())
                    .addValueEventListener(postListener)

            }

            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()

        }
        dlg.setPositiveButton("취소", null)
        dlg.show()

    }
fun addTask(){

       list_itemText.clear()
         Log.d("비교", dateTV.text.toString())
         list_item.add(ListViewModel(editText.text.toString(),"false"))

         var et = editText.text.toString()
       Log.d("보기",et.toString())
         //FB에 넣기
         UserApiClient.instance.me { user, error ->
             fbkey = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
             FBRef.todoDate
                 .child(user!!.id.toString())
                 .child(dateTV.text.toString())
                 .push()
                 .setValue(ListViewModel(et,"false"))
             list_itemText.add(et)
         }

         editText.text.clear()

}

    fun clearBtn(){
        var dlg = AlertDialog.Builder(this)
        dlg.setTitle("growingPlant")
        dlg.setMessage("전체 일정을 삭제하시겠습니까?")
        dlg.setIcon(R.drawable.img_2)

        dlg.setNegativeButton("삭제") { dialog, which ->


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

            Toast.makeText(this, "초기화 되었습니다.", Toast.LENGTH_SHORT).show()

        }
        dlg.setPositiveButton("취소", null)
        dlg.show()

    }


fun getCommentData(date: String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
  val postListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
          list_item.clear()
          list_itemtext.clear()
          textList.clear()
          for (dataModel in dataSnapshot.children) {
              if (date == dataModel.key) {

                  for (i in dataModel.children) {
                      Log.d("확인list2", i.value.toString())
                      Log.d("확인list2", i.key.toString())
                      //val item = i.getValue(ListViewModel::class.java)
                      //  Log.d("확인인",i.getValue(ListViewModel::class.java).toString())
                      list_item.add(i.getValue(ListViewModel::class.java)!!)
                      textList.add(i.getValue(ListViewModel::class.java)!!.text.toString())
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


}
