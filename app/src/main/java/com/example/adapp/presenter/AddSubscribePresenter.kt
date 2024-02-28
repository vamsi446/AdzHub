package com.example.adapp.presenter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddSubscribePresenter(val view: View) {

    var user = FirebaseAuth.getInstance().currentUser


    fun addSubscribe(category: String){
        if(user!=null)
        {
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(user.uid)
            ref.child("category").setValue(category)
                    .addOnCompleteListener {
                        view.sendToast("You have subscribed to $category")
                    }
                    .addOnFailureListener {
                        view.sendToast("Your subscription is unsuccessful")
                    }
        }

    }

    interface View{
        fun sendToast(message: String)
    }
}