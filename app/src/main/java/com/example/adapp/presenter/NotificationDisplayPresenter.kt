package com.example.adapp.presenter

import com.example.adapp.model.Ad_response
import com.example.adapp.model.Advertisement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NotificationDisplayPresenter(val view: View) {

    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    val user = FirebaseAuth.getInstance().currentUser
    private val adsRef: DatabaseReference = rootRef.child("Notification").child(user.uid)

    fun getNotification(callback: RetrieveAdsCallback){
        adsRef.get().addOnCompleteListener { task ->
            val response = Ad_response()
            if(task.isSuccessful){
                val result = task.result
                result?.let {
                    response.listOfAds = result.children.map {
                        it.getValue(Advertisement::class.java)!!
                    }
                }
            }
            else{
                view.sendToast("Not able to get the Ads")
            }
            callback.onResponse(response)
        }
    }

    interface View{
        fun sendToast(message: String)
    }
}