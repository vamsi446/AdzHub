package com.example.adapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bumptech.glide.Glide
import com.example.adapp.R
import com.example.adapp.model.Advertisement
import kotlinx.android.synthetic.main.fragment_add_details.*
import kotlinx.android.synthetic.main.fragment_my_ads_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "adDetails"

/**
 * A simple [Fragment] subclass.
 * Use the [AddDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var ad: Advertisement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ad = it.getSerializable(ARG_PARAM1) as Advertisement
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adDetailsTitleT.text = ad?.title
        adDetailPriceT.text = "₹ ${ad?.price}"
        adDetailDescT.text = ad?.description
        adDetailLocT.text = ad?.location
        adDetailContactT.text=ad?.contact
        adCallContact.setOnClickListener{
            val callIntent= Intent(Intent.ACTION_DIAL,Uri.parse("tel:${ad?.contact}"))
            startActivity(callIntent)


        }
        adMessageContact.setOnClickListener{
            val msgIntent=Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:${ad?.contact}"))
            startActivity(msgIntent)

        }

        if(ad?.imageUrl != null) {
            Glide.with(view.context).load(Uri.parse(ad?.imageUrl)).into(adDetailImg)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Advertisement) =
                AddDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, param1)
                    }
                }
    }
}