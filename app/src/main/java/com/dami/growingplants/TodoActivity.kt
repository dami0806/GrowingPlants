package com.dami.growingplants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.ImageView
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
                // This is because every time when you add the item the input space or the eidt text space will be cleared
                editText.text.clear()
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