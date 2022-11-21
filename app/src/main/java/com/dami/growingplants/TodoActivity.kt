package com.dami.growingplants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_todo.*

class TodoActivity : AppCompatActivity() {
    lateinit var calBtn: ImageView
    lateinit var toDoBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

            var itemlist = arrayListOf<String>()
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
                   /* val title = binding.titleArea.text.toString()
                    val content = binding.contentArea.text.toString()
                    val user = user!!.kakaoAccount!!.email
                    val time = KakaoAuth.getTime()
val key = FBRef.boardRef.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴

                    if(isImgUpload==true){
                        // binding.imgArea.visibility= View.VISIBLE
                        imgUpload(key)}
                    /*   Log.d(TAG,title)
                Log.d(TAG,content)*/

                    */

                    //board - key - boardModel(데이터 title,content,uid,time)
                    val key = FBRef.todoDate.push().key.toString() //이미지이름에 쓰려고 먼저 키값 받아옴
                    FBRef.todoDate
                        .child(key)
                        .setValue("날짜넣을거임")
                   


                }


                }


            // Clear버튼 클릭
            clear.setOnClickListener {

                itemlist.clear()
                adapter.notifyDataSetChanged()
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
            }


        calBtn = findViewById(R.id.CalBtn)
        toDoBtn = findViewById(R.id.TodoBtn)
        calBtn.setOnClickListener {
            var intent = Intent(this,HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


    }


}