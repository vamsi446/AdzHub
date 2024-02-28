package com.example.adapp.view

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.adapp.R
import com.example.adapp.view.all_ads.AllAdsFragment
import com.example.adapp.view.my_account.MyAccountFragment
import com.example.adapp.view.my_ads.MyAdsFragment
import com.example.adapp.view.notification.NotificationFragment
import kotlinx.android.synthetic.main.activity_nav_activity.*

class Nav_activity : AppCompatActivity() {

    //lateinit var bottomNavigationView : BottomNavigationView
    lateinit var  transaction:FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_activity)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        //initializing all fragments
        val allAdsFragment = AllAdsFragment()
        setCurrentFragment(allAdsFragment)

        //click listener to bottom navigation bar items
        bottomNavigationView.setOnNavigationItemSelectedListener {
            val isOnline=isOnline()

            when(it.itemId){
                R.id.home -> {
                    bottomNavigationView.getMenu().setGroupCheckable(0,true,true)

                    if(fab.visibility==View.INVISIBLE)
                    {
                        fab.visibility=View.VISIBLE
                    }
                    if (isOnline) {
                        val allAdsFragment = AllAdsFragment()
                        setCurrentFragment(allAdsFragment)
                    }
                    else
                    {
                        val noInternetFragment=NoInternetFragment()
                        setCurrentFragment(noInternetFragment)
                    }
                }


                R.id.myAds ->{
                    bottomNavigationView.getMenu().setGroupCheckable(0,true,true)

                    if(fab.visibility==View.INVISIBLE)
                    {
                        fab.visibility=View.VISIBLE
                    }
                    if (isOnline) {
                        val myAdsFragment = MyAdsFragment()
                        setCurrentFragment(myAdsFragment)
                    }
                    else
                    {
                        val noInternetFragment=NoInternetFragment()
                        setCurrentFragment(noInternetFragment)
                    }

                }
                R.id.account ->{
                    bottomNavigationView.getMenu().setGroupCheckable(0,true,true)

                    if(fab.visibility==View.INVISIBLE)
                    {
                        fab.visibility=View.VISIBLE
                    }
                    if (isOnline) {
                        val myAccountFragment = MyAccountFragment()
                        setCurrentFragment(myAccountFragment)
                    }
                    else
                    {
                        val noInternetFragment=NoInternetFragment()
                        setCurrentFragment(noInternetFragment)
                    }

                }
                R.id.notifications ->{
                    bottomNavigationView.getMenu().setGroupCheckable(0,true,true)

                    if(fab.visibility==View.INVISIBLE)
                    {
                        fab.visibility=View.VISIBLE
                    }
                    if (isOnline) {
                        val notificationFragment = NotificationFragment()
                        setCurrentFragment(notificationFragment)
                    }
                    else
                    {
                        val noInternetFragment=NoInternetFragment()
                        setCurrentFragment(noInternetFragment)
                    }

                }
            }
            true
        }

    }


    //common function to set fragment to constraint layout
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment,fragment)
            commit()
        }
    fun addAdvertisement(view:View){
        bottomNavigationView.getMenu().setGroupCheckable(0,false,true)

        fab.visibility=View.INVISIBLE
        val newAdFragment = New_ad_home()
        setCurrentFragment(newAdFragment)
    }
    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected==true
    }
}