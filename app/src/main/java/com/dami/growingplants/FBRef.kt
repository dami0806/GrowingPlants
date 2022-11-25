package com.dami.growingplants

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
    private val database = Firebase.database
    val UserInfo = database.getReference("userInfo")
        val checkmark = database.getReference("checkmark")
    val todoDate = database.getReference("Date")
//TESTING

}
}