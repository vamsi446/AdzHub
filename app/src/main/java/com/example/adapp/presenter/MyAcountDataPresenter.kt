package com.example.adapp.presenter

import android.util.Log
import android.widget.Toast
import com.example.adapp.model.Response
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_my_account.*

class MyAcountDataPresenter(val view:View) {

    private val rootRef = FirebaseDatabase.getInstance().reference
    private val userRef = rootRef.child("Users")

    fun getAccountDetails(callback: FirebaseCallback){

        val user = FirebaseAuth.getInstance().currentUser
        userRef.child(user.uid).get().addOnCompleteListener { task ->
            val response = Response()
            if(task.isSuccessful){
                val result = task.result
                result?.let {
                    response.user = it.getValue(User::class.java)!!
                }
            }
            else{
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }
    interface View{
        fun sendToast(message: String)
    }

}