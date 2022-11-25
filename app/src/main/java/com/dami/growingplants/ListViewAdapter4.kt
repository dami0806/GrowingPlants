package com.dami.growingplants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView

class ListViewAdapter4(val List: MutableList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView


        if(convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }

        val title = convertView!!.findViewById<TextView>(R.id.title)
        val checkimg = convertView!!.findViewById<ImageView>(R.id.checkimg)
        title.text = List[position]

        checkimg.setImageResource(R.drawable.nocheck)
        if(title.text.contains("true")){
            checkimg.setImageResource(R.drawable.check)
        }
        else if(title.text.contains("false")){
            checkimg.setImageResource(R.drawable.nocheck)
        }

        //checkimg.setImageResourc.e(R.drawable.nocheck)





        return convertView!!

    }
}