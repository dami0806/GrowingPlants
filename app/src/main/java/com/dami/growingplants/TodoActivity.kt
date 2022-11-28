package com.dami.growingplants

import android.content.Intent
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
    lateinit var editText: EditText
    private lateinit var listviewAdapter: ListViewAdapter4

    lateinit var add: Button
    lateinit var clear: Button
    lateinit var delete: Button
    var d:String?=null
    val list_item = mutableListOf<String>()
    var textList = arrayListOf<String>()
    var checkList = arrayListOf<String>()
    val list_itemkey = mutableListOf<String>()
    private lateinit var key: String
    private lateinit var idkey: ArrayList<String>
    private var checkmarkIdList = mutableListOf<String>()
    var count:Int =0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val listview = findViewById<ListView>(R.id.listView)
        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
        clear = findViewById(R.id.clear)
        delete = findViewById(R.id.delete)
        add = findViewById(R.id.add)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val dateTV = current.format(formatter).toString()

        key = intent.getStringExtra("dateKey").toString()

        editText = findViewById(R.id.editText)
        listviewAdapter = ListViewAdapter4(list_item)
       // listviewAdapter = ListViewAdapter4(list_itemkey)
        listview.adapter = listviewAdapter
        getCommentData(dateTV)
        TextData(dateTV)
        listview.setOnItemClickListener { parent, view, position, id ->
            Log.d("화긴",list_item[position])
            Log.d("화긴",position.toString())
            Log.d("담담3", textList.toString())
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //list_item.clear()
                    count =0
                    for (dataModel in dataSnapshot.children) {
                        //position번째

                        if(count==position){

                            //true 로 바꾸기
                            if(checkList[position]=="false"){
                            UserApiClient.instance.me { user, error ->
                                FBRef.todoDate
                                    .child(user!!.id.toString())
                                    .child(dateTV.toString())
                                    .child(list_itemkey[position])
                                   .setValue(ListViewModel(textList[position],true))

                            }}
                            //false 로 바꾸기
                            if(checkList[position]=="true"){
                            UserApiClient.instance.me { user, error ->
                                FBRef.todoDate
                                    .child(user!!.id.toString())
                                    .child(dateTV.toString())
                                    .child(list_itemkey[position])
                                    .setValue(ListViewModel(textList[position],false))

                            }}
                            break
//true는 check로

                        listviewAdapter.notifyDataSetChanged()
                    }
                        count++
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

        clear.setOnClickListener { clearBtn()}
        delete.setOnClickListener {  }

        //checkmarkData()


        //탭
        toDoBtn.setOnClickListener {
        }
        calBtn.setOnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }






fun clearBtn(){


       // list_item.clear()
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
    fun getCommentData(date:String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_item.clear()
                list_itemkey.clear()
                for (dataModel in dataSnapshot.children) {
                    if (date == dataModel.key) {

                        for (i in dataModel.children) {

                            list_item.add(i.getValue(ListViewModel::class.java)!!.text.toString())
                            list_itemkey.add(i.key.toString())
                            Log.d("뭘가요",list_item.toString())
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


fun TextData(dateTV:String) {
    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            for (dataModel in dataSnapshot.children) {


                for (i in dataModel.children) {
                    //Log.d("담담",i.value.toString())
                    Log.d("담담", i.toString())

                    var item = dataModel.value
                    var itemText = dataModel.getValue(ListViewModel::class.java)!!.text.toString()
                    var itemClick =dataModel.getValue(ListViewModel::class.java)!!.click .toString()
                    Log.d("이거", item.toString())
                    Log.d("이거11", itemText)
                    Log.d("이거11", itemClick)

                    textList.add(itemText)
                    checkList.add(itemClick)

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
