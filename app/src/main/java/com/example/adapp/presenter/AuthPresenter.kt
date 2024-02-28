package com.example.adapp.presenter

import android.net.Uri
import android.widget.Toast
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AuthPresenter(val view: View){

    val ref = FirebaseStorage.getInstance().getReference("Users")

    fun createAccount(username: String, email: String, password: String, phoneNo: String, imgUri: Uri?):Boolean{
        val fAuth= FirebaseAuth.getInstance()
        var flag=true

        if(imgUri != null) {
            val fileRef =
                ref.child("${System.currentTimeMillis()}.${view.getFileExtension(imgUri)}")
            fileRef.putFile(imgUri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        fAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = fAuth.currentUser
                                    val userObj =
                                        User(username, email, password, phoneNo, uri.toString())
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(user.uid).setValue(userObj)
                                } else {
                                    flag = false
                                }
                            }
                    }
                }
                .addOnFailureListener {
                    view.sendToast("User details couldn't be uploaded")
                }
        }
        else{
            fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = fAuth.currentUser
                        val userObj =
                            User(username, email, password, phoneNo, " ")
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.uid).setValue(userObj)
                    } else {
                        flag = false
                    }
                }
        }
        return flag
    }

    fun loginUser(userMail: String, userPassword: String):Boolean{
        val lAuth = FirebaseAuth.getInstance()
        var flag = true
        lAuth.signInWithEmailAndPassword(userMail,userPassword)
            .addOnCompleteListener() { task->
                if(task.isSuccessful)
                {
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user.isEmailVerified)
                    {
                        view.sendToast("Logged In successfully")
                    }
                    else
                    {
                        user.sendEmailVerification()
                        view.sendToast("Verification mail sent..")
                        flag = false
                    }
                }
                else
                {
                    view.sendToast("Failed to login")
                    flag = false
                }
            }
        return flag
    }
    fun updateData(changedPhoneNumber: String, changedUserName: String, imgUri: Uri?) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user.uid
        if(imgUri == null) {
            val databaseRef = FirebaseDatabase.getInstance()
                .getReference("Users").child(uid)
            databaseRef.child("username").setValue(changedUserName)
            databaseRef.child("phoneNumber").setValue(changedPhoneNumber)
            view.sendToast("Changes made successfully")
            //Toast.makeText(activity,"Changes made successfully",Toast.LENGTH_SHORT).show()
        }
        else{
            val fileRef = ref.child("${System.currentTimeMillis()}.${view.getFileExtension(imgUri)}")
            fileRef.putFile(imgUri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        val databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                        databaseRef.child("username").setValue(changedUserName)
                        databaseRef.child("phoneNumber").setValue(changedPhoneNumber)
                        databaseRef.child("imageUrl").setValue(uri.toString())
                        view.sendToast("Changes made successfully")
                    }
                }
                .addOnFailureListener {
                    view.sendToast("Changes are not made")
                }
        }
    }
    fun resetPassword(email:String)
    {
        val auth=FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            view.sendToast("Check your email to reset Password!")
        }.addOnFailureListener{
            view.sendToast("Something went wrong. Try again!")
        }

    }


    interface View{
        fun sendToast(message: String)
        fun getFileExtension(uri: Uri): String?
    }
}