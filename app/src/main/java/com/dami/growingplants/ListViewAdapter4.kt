package com.dami.growingplants

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView

class ListViewAdapter4(val List: MutableList<ListViewModel>) : BaseAdapter() {

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
        val click = convertView!!.findViewById<TextView>(R.id.click)
        val checkimg = convertView!!.findViewById<ImageView>(R.id.checkimg)
        title.text = List[position].text
        click.text= List[position].click
        Log.d("적용",click.text.toString())

        if(click.text.toString()=="true"){

            checkimg.setImageResource(R.drawable.check)
        }
        else if(click.text.toString()=="false"){
            checkimg.setImageResource(R.drawable.nocheck)
        }

        //checkimg.setImageResourc.e(R.drawable.nocheck)





        return convertView!!

    }
}