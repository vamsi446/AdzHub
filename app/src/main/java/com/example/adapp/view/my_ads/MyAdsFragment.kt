package com.example.adapp.view.my_ads

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.model.Ad_response
import com.example.adapp.model.Advertisement
import com.example.adapp.presenter.AdDisplayPresenter
import com.example.adapp.presenter.RetrieveAdsCallback
import com.example.adapp.view.NoAddsFragment

/**
 * A fragment representing a list of Items.
 */
class MyAdsFragment : Fragment(), AdDisplayPresenter.View, RetrieveAdsCallback {

    private var columnCount = 1
    lateinit var displayPresenter: AdDisplayPresenter
    var adsList = listOf<Advertisement>()
    lateinit var rList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        displayPresenter = AdDisplayPresenter(this)
        displayPresenter.getAllAds(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_my_ads, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                /*layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }*/
                rList = view
                layoutManager=GridLayoutManager(context,1)
                //adapter = MyAdsRecyclerViewAdapter(adsList)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MyAdsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onResponse(response: Ad_response) {
        val listAds = mutableListOf<Advertisement>()
        val adlist = response.listOfAds
        val uid = displayPresenter.getUid()
        if(uid != null){
            adlist?.forEach {
                if(it.uid == uid){
                    listAds.add(it)
                }
            }
            adsList = listAds
        }
        rList.adapter?.notifyDataSetChanged()
        if(adsList.isEmpty())
        {
            val noAdsFragment=NoAddsFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment,noAdsFragment).commit()

        }
        else {
            rList.adapter = MyAdsRecyclerViewAdapter(adsList) {
                val myAd = MyAdsDetails.newInstance(it)
                activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment, myAd)
                        ?.addToBackStack(null)
                        ?.commit()

            }
        }

    }
}