package com.dami.growingplants

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
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
                    (year.toString() + "??? " + (month + 1).toString() + "??? " + dayOfMonthString + "???")

                // set this date in TextView for Display
                dateTV.setText(Datetext)
                Log.d("????????????",dayOfMonthString)
                Log.d("????????????",dayOfMonth.toString())
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
        dlg.setMessage("??? ????????? ?????????????????????????")
        dlg.setIcon(R.drawable.img_2)

        dlg.setNegativeButton("??????") { dialog, which ->


            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataModel in dataSnapshot.children) { //datModel: key???

                        var selectitem=textList[position]
                        Log.d("selectitem",selectitem.toString())
                        Log.d("selectitem",dataModel.getValue(ListViewModel::class.java)!!.text.toString())
                        if (selectitem == dataModel.getValue(ListViewModel::class.java)!!.text.toString()) //????????? list[postiion]time??? dateModel??? value time ????????? ??????
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
                fbkey = FBRef.todoDate.push().key.toString() //?????????????????? ????????? ?????? ?????? ?????????
                FBRef.todoDate
                    .child(user!!.id.toString())
                    .child(dateTV.text.toString())
                    .addValueEventListener(postListener)

            }

            Toast.makeText(this, "?????????????????????.", Toast.LENGTH_SHORT).show()

        }
        dlg.setPositiveButton("??????", null)
        dlg.show()

    }
fun addTask(){


       list_itemText.clear()
         Log.d("??????", dateTV.text.toString())
         list_item.add(ListViewModel(editText.text.toString(),"false"))

         var et = editText.text.toString()
       Log.d("??????",et.toString())
         //FB??? ??????
         UserApiClient.instance.me { user, error ->
             fbkey = FBRef.todoDate.push().key.toString() //?????????????????? ????????? ?????? ?????? ?????????
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
        dlg.setMessage("?????? ????????? ?????????????????????????")
        dlg.setIcon(R.drawable.img_2)

        dlg.setNegativeButton("??????") { dialog, which ->


            list_item.clear()
            listviewAdapter.notifyDataSetChanged()
            UserApiClient.instance.me { user, error ->

                FBRef.todoDate
                    .child(user!!.id.toString())
                    .child(dateTV.text.toString())
                    .removeValue()
            }
            getCommentData(dateTV.text.toString())
            listviewAdapter.notifyDataSetChanged()//????????? ?????????

            Toast.makeText(this, "????????? ???????????????.", Toast.LENGTH_SHORT).show()

        }
        dlg.setPositiveButton("??????", null)
        dlg.show()

    }


fun getCommentData(date: String) {
//comment ?????? board?????? commentkey ?????? comment ????????????
  val postListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
          list_item.clear()
          list_itemtext.clear()
          textList.clear()
          for (dataModel in dataSnapshot.children) {
              if (date == dataModel.key) {

                  for (i in dataModel.children) {
                      Log.d("??????list2", i.value.toString())
                      Log.d("??????list2", i.key.toString())
                      //val item = i.getValue(ListViewModel::class.java)
                      //  Log.d("?????????",i.getValue(ListViewModel::class.java).toString())
                      list_item.add(i.getValue(ListViewModel::class.java)!!)
                      textList.add(i.getValue(ListViewModel::class.java)!!.text.toString())
                      keyList.add(i.key.toString())
                      for (j in i.children) {

                          Log.d("??????list3", j.value.toString())
                          if(j.key.toString().contains("text=")){
                              list_itemtext.add(j.key.toString())
                          }
                      }

                  }}
              listviewAdapter.notifyDataSetChanged()//????????? ?????????
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
