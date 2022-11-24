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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoActivity : AppCompatActivity() {
    lateinit var calBtn:ImageView
    lateinit var toDoBtn:ImageView
    private lateinit var listviewAdapter: ListViewAdapter4
    val items = mutableListOf<ListViewModel>()

    lateinit var dateTV: String
    val list_item = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)


        val listview = findViewById<ListView>(R.id.listView)
        listviewAdapter = ListViewAdapter4(list_item)
        listview.adapter = listviewAdapter

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val  dateTV= current.format(formatter).toString()
        Log.d("왕",dateTV.toString())
        getCommentData(dateTV)






        toDoBtn.setOnClickListener {

        }
        calBtn.setOnClickListener {
            var intent = Intent(this,HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }
    }


    fun getCommentData(date:String) {
//comment 아래 board아래 commentkey 아래 comment 데이터들
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_item.clear()
                for (dataModel in dataSnapshot.children) {
                    if (date == dataModel.key) {
                        Log.d("확인1", date.toString())


                        Log.d("확인", dataModel.key.toString())
                        Log.d("확인list", list_item.toString())
                        for (i in dataModel.children) {
                            Log.d("확인list2", i.value.toString())
                            list_item.add(i.value.toString())
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


         /*   var itemlist = arrayListOf<String>()
            var adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice
                , itemlist)



            // add버튼 클릭
            add.setOnClickListener {
                itemlist.add(editText.text.toString())
                listView.adapter =  adapter
                adapter.notifyDataSetChanged()

                editText.text.clear()

                //FB에 넣기
                UserApiClient.instance.me { user, error ->
                   */// val title = binding.titleArea.text.toString()
// val content = binding.contentArea.text.toString()
// val user = user!!.kakaoAccount!!.email
// val time = KakaoAuth.getTime()
// val key = FBRef.boardRef.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
//
// if(isImgUpload==true){
// // binding.imgArea.visibility= View.VISIBLE
// imgUpload(key)}
// /*   Log.d(TAG,title)
// Log.d(TAG,content)*//*
//
// *//*
//
// //board - key - boardModel(데이터 title,content,uid,time)
// val key = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
// FBRef.todoDate
// .child(key)
// .setValue("날짜넣을거임")
//
//
//
// }
//
//
// }
//
//
// // Clear버튼 클릭
// clear.setOnClickListener {
//
// itemlist.clear()
// adapter.notifyDataSetChanged()
// }
// //
// listView.setOnItemClickListener { adapterView, view, i, l ->
// android.widget.Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), android.widget.Toast.LENGTH_SHORT).show()
// }
// // 삭제버튼 클릭
// delete.setOnClickListener {
// val position: SparseBooleanArray = listView.checkedItemPositions
// val count = listView.count
// var item = count - 1
// while (item >= 0) {
// if (position.get(item))
// {
// adapter.remove(itemlist.get(item))
// }
// item--
// }
// position.clear()
// adapter.notifyDataSetChanged()
// }
//
//
//Tap




