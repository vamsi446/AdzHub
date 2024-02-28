package com.example.adapp.presenter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.adapp.R
import com.example.adapp.model.Advertisement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdCheckPresenter(val context: Context, params: WorkerParameters) : Worker(context, params){

    var userCategory = ""
    var user = FirebaseAuth.getInstance().currentUser

    fun checkUserCategory() {
        if(user != null && isOnline()) {
            val userId = user.uid
            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("category").get().addOnCompleteListener {
                val result = it.result
                result.let {
                    userCategory = it?.value as String
                    checkAds()
                }
            }
        }
    }
    val database = FirebaseDatabase.getInstance().getReference("Advertisements")
    val notifyRef = FirebaseDatabase.getInstance().getReference("Notification")

    fun checkAds() {
        Log.d("MainActivity", userCategory)
        database.orderByKey().addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newCount = snapshot.childrenCount
                    Log.d("MainActivity", "Db Count:$newCount")
                    val pref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
                    val count = pref.getInt("userCount", 0)
                    Log.d("MainActivity", "pref Count:$count")
                    val ads = snapshot.children.map {
                        it.getValue(Advertisement::class.java)
                    }
                    Log.d("MainActivity", "Ad Category : ${ads.last()?.category}")
                    if (newCount > count) {
                        Log.d("MainActivity", "New user added")
                        if(userCategory == ads.last()?.category) {
                            sendNotification("New ad added")
                            addNotification(ads.last()!!)
                        }
                    }
                    saveCount(newCount.toInt())
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun saveCount(count: Int){
        val pref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("userCount", count)
        editor.commit()
    }

    private fun sendNotification(text: String) {

        val nManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var builder : Notification.Builder

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Test", "AdApp",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            nManager.createNotificationChannel(channel)
            builder = Notification.Builder(context, "Test")
        }
        else{
            builder = Notification.Builder(context)
        }

        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
        builder.setContentTitle("AdApp")
        builder.setContentText(text)
        builder.setAutoCancel(true)

        val myNotification = builder.build()

        nManager.notify(1, myNotification)
    }

    private fun addNotification(ad: Advertisement){
        notifyRef.child(user.uid).push().setValue(ad)
    }

    override fun doWork(): Result {
        checkUserCategory()
        return Result.success()
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected==true
    }
}