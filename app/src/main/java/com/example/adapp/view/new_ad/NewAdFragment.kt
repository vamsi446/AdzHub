package com.example.adapp.view.new_ad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.os.bundleOf
import com.example.adapp.R
import androidx.navigation.fragment.findNavController
import com.example.adapp.model.Advertisement
import com.example.adapp.view.new_ad_details
import kotlinx.android.synthetic.main.activity_bottom__navigation_.*

import kotlinx.android.synthetic.main.fragment_new_ad.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM2 = "ad"

/**
 * A simple [Fragment] subclass.
 * Use the [NewAdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewAdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var advert: Advertisement? = null
    lateinit var category_selected : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            advert = it.getSerializable(ARG_PARAM2) as Advertisement
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //handling click of each image button
            view.findViewById<ImageButton>(R.id.mobile_category_btn).setOnClickListener {
                category_selected = "mobile"
                navigate_to_details(category_selected)
            }
            view.findViewById<ImageButton>(R.id.vehicle_category_btn).setOnClickListener {
                category_selected = "vehicle"
                navigate_to_details(category_selected)
            }
            view.findViewById<ImageButton>(R.id.furniture_category_btn).setOnClickListener {
                category_selected = "furniture"
                navigate_to_details(category_selected)
            }
            view.findViewById<ImageButton>(R.id.electronics_category_btn).setOnClickListener {
                category_selected = "electronics"
                navigate_to_details(category_selected)
            }
            view.findViewById<ImageButton>(R.id.property_category_btn).setOnClickListener {
                category_selected = "property"
                navigate_to_details(category_selected)
            }
            view.findViewById<ImageButton>(R.id.other_category_btn).setOnClickListener {
                category_selected = "other"
                navigate_to_details(category_selected)
            }
    }


    //creating bundle aand navigating to ad_details_fragment
    private fun navigate_to_details(category_selected:String){
        if(advert == null) {
            val bundle = bundleOf("category" to category_selected)
            findNavController().navigate(R.id.action_newAdFragment_to_new_ad_details, bundle)
        }
        else{
            //Toast.makeText(activity, "$advert", Toast.LENGTH_LONG).show()
            val adDetail = new_ad_details.newInstance(advert!!, category_selected)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, adDetail)
                    ?.addToBackStack(null)
                    ?.commit()
//            val bundle = bundleOf("category" to category_selected, "flag" to 1, "advert" to advert)
//            findNavController().navigate(R.id.action_newAdFragment_to_new_ad_details, bundle)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewAdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param: Advertisement) =
            NewAdFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM2, param)
                }
            }
    }
}