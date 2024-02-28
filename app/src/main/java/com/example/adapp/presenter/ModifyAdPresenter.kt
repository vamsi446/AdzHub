package com.example.adapp.presenter

import com.google.firebase.database.FirebaseDatabase

class ModifyAdPresenter(val view: View) {

    val mRef  = FirebaseDatabase.getInstance().getReference("Advertisements")

    fun deleteAdvertisement(key: String){
        mRef.child(key).removeValue()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        view.sendToast("Ad is deleted successfully")
                        view.stopProgressBar()
                    }
                    else{
                        view.sendToast("Deletion of add failed")
                        view.stopProgressBar()
                    }
                }
    }

    interface View{
        fun sendToast(message: String)
        fun stopProgressBar()
    }
}