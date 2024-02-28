package com.example.adapp.view.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.model.Ad_response
import com.example.adapp.presenter.NotificationDisplayPresenter
import com.example.adapp.presenter.RetrieveAdsCallback
import com.example.adapp.view.AddDetailsFragment

/**
 * A fragment representing a list of Items.
 */
class NotificationFragment : Fragment(), NotificationDisplayPresenter.View, RetrieveAdsCallback {

    private var columnCount = 1
    lateinit var notificationDisplayPresenter: NotificationDisplayPresenter
    lateinit var rView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        notificationDisplayPresenter = NotificationDisplayPresenter(this)
        notificationDisplayPresenter.getNotification(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_notification, container, false)

        rView = view.findViewById(R.id.rView)
        rView.layoutManager = LinearLayoutManager(context)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
                //adapter = NotificationRecyclerViewAdapter(DummyContent.ITEMS)
                rView = view
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
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onResponse(response: Ad_response) {
        Log.d("NotificationFragment", "${response.listOfAds}")
        rView.adapter = NotificationRecyclerViewAdapter(response.listOfAds!!){
            val adDisplay = AddDetailsFragment.newInstance(it)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment, adDisplay)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
}