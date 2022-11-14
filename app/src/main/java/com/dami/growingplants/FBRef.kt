package com.dami.growingplants

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    private val database = Firebase.database
    val UserInfo = database.getReference("userInfo")
//TESTING

}