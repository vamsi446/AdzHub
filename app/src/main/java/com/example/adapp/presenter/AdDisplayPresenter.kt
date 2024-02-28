package com.example.adapp.presenter

import com.example.adapp.model.Ad_response
import com.example.adapp.model.Advertisement
import com.example.adapp.model.Response
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdDisplayPresenter(val view: View) {

    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val adsRef: DatabaseReference = rootRef.child("Advertisements")
    val user = FirebaseAuth.getInstance().currentUser

    fun getAllAds(callback: RetrieveAdsCallback){
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

    fun getUid(): String?{
        return user.uid
    }

    interface View{
        fun sendToast(message: String)
    }
}