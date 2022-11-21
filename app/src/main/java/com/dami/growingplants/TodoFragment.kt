package com.dami.growingplants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.dami.growingplants.R


class TodoFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

      /*  val view = inflater.inflate(R.layout.fragment_todo, container, false)
        view?.findViewById<ImageView>(R.id.CalBtn)?.setOnClickListener {
            it.findNavController().navigate(R.id.action_todoFragment2_to_calenderFragment2)
        }*/
        return view
    }


}