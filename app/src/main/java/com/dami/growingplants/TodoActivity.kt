package com.dami.growingplants

import android.content.Intent
import android.icu.text.Transliterator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.listview_item.*
import kotlinx.android.synthetic.main.listview_item.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoActivity : AppCompatActivity() {
    lateinit var calBtn: ImageView
    lateinit var toDoBtn: ImageView
    lateinit var alarmBtn: ImageView
    lateinit var editText: EditText
    private lateinit var listviewAdapter: ListViewAdapter4
    lateinit var add: ImageView
    var dateTV:String = ""
    val list_item = mutableListOf<ListViewModel>()
    var textList = arrayListOf<String>()
    var checkList = arrayListOf<String>()
    val list_itemkey = mutableListOf<String>()
    private lateinit var key: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val listview = findViewById<ListView>(R.id.listView)
        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
        alarmBtn = findViewById(R.id.AlarmBtn)
        add = findViewById(R.id.add)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        dateTV = current.format(formatter).toString()

        key = intent.getStringExtra("dateKey").toString()

        editText = findViewById(R.id.editText)
        listviewAdapter = ListViewAdapter4(list_item)

        listview.adapter = listviewAdapter
        getCommentData(dateTV)


        listview.setOnItemClickListener { parent, view, position, id ->
          changeClick(position)
        }

        //탭
        toDoBtn.setOnClickListener {
        }
        calBtn.setOnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        alarmBtn.setOnClickListener {
            var intent = Intent(this, AlarmActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }
    }

fun changeClick(position:Int){
    var listposition = checkList[position]
    var textposition = textList[position]
    UserApiClient.instance.me { user, error ->
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    if (listposition.contains("true")) {

                        FBRef.todoDate
                            .child(user!!.id.toString())
                            .child(dateTV.toString())
                            .child(list_itemkey[position])
                            .setValue(ListViewModel(textposition, click = "false"))
                            checkList[position] = "false"

                        break

                    }

                    //true 로 바꾸기
                    else if (listposition.contains("false")) {

                        FBRef.todoDate
                            .child(user!!.id.toString())
                            .child(dateTV.toString())
                            .child(list_itemkey[position])
                            .setValue(ListViewModel(textposition, click = "true"))

                        break
                    }
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        UserApiClient.instance.me { user, error ->
            FBRef.todoDate.child(user!!.id.toString())
                .child(dateTV)
                .addValueEventListener(postListener)
        }
//title이 같은애들일때 check로 바뀜

    }
    listviewAdapter.notifyDataSetChanged()
}
    fun clearBtn() {

        listviewAdapter.notifyDataSetChanged()
        UserApiClient.instance.me { user, error ->
            FBRef.todoDate
                .child(user!!.id.toString())
                .child(key)
                .removeValue()

        }
        getCommentData(key)
    }

    //오늘날짜 todolist에 가져오기
    fun getCommentData(date: String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_item.clear()
                list_itemkey.clear()
                for (dataModel in dataSnapshot.children) {
                    if (date == dataModel.key) {

                        for (i in dataModel.children) {

                            list_item.add(
                                ListViewModel(
                                    i.getValue(ListViewModel::class.java)!!.text.toString(),
                                    i.getValue(ListViewModel::class.java)!!.click.toString()
                                )
                            )
                            list_itemkey.add(i.key.toString())
                            textList.add(i.getValue(ListViewModel::class.java)!!.text.toString())
                            checkList.add(i.getValue(ListViewModel::class.java)!!.click.toString())
                            Log.d("뭘가요", list_item.toString())

                        }
                    }
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
