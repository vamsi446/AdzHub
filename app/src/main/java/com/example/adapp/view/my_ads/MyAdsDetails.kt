package com.example.adapp.view.my_ads

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.adapp.R
import com.example.adapp.model.Advertisement
import com.example.adapp.view.new_ad.NewAdFragment
import com.example.adapp.presenter.ModifyAdPresenter
import kotlinx.android.synthetic.main.fragment_add_details.*
import kotlinx.android.synthetic.main.fragment_my_ads_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "myAdDetails"
/**
 * A simple [Fragment] subclass.
 * Use the [MyAdsDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAdsDetails : Fragment(), ModifyAdPresenter.View {
    // TODO: Rename and change types of parameters
    private var myAd: Advertisement? = null
    lateinit var modifyAdPresenter: ModifyAdPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myAd = it.getSerializable(ARG_PARAM1) as Advertisement
        }
        modifyAdPresenter = ModifyAdPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_ads_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adDetailsProgressB.visibility = View.INVISIBLE
        adDetailsTitleTV.text = myAd?.title
        adDetailsPriceTV.append(" ${myAd?.price}")
        adDetailsCategoryTV.append(" ${myAd?.category}")
        adDetailsBrandTV.append(" ${myAd?.brand}")
        adDetailsDescriptionTV.append(" ${myAd?.description}")
        adDetailsLocationTV.append(" ${myAd?.location}")
        adDetailsContactTV.append(" ${myAd?.contact}")

        Glide.with(view.context).load(Uri.parse(myAd?.imageUrl)).into(adDetailsIV)

        adDetailsDeleteB.setOnClickListener {
            adDetailsProgressB.visibility = View.VISIBLE
            modifyAdPresenter.deleteAdvertisement(myAd?.key!!)
        }

        adDetailsEditB.setOnClickListener {
            val newAds = NewAdFragment.newInstance(myAd!!)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, newAds)
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAdsDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Advertisement) =
            MyAdsDetails().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun stopProgressBar() {
        adDetailsProgressB.visibility = View.INVISIBLE
        val myAds = MyAdsFragment.newInstance(0)
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment, myAds)
                ?.commit()
    }
}