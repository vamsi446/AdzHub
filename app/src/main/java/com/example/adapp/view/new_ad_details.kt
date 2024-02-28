package com.example.adapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.adapp.R
import com.example.adapp.model.Advertisement
import kotlinx.android.synthetic.main.fragment_new_ad_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [new_ad_details.newInstance] factory method to
 * create an instance of this fragment.
 */
class new_ad_details : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var advert: Advertisement? = null
    private var category_selected: String? = null
    private var category: String? = null
    var itemSelected = true
    var item: String? = null
    var brand_category = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category_selected = it.getString(ARG_PARAM2)
            if(category_selected != null) {
                advert = it.getSerializable(ARG_PARAM1) as Advertisement
            }

            category = it.getString("category")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_ad_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(advert != null){
            titleET.setText(advert?.title)
            descET.setText(advert?.description)
            priceET.setText(advert?.price)
        }

        val spinner =  view.findViewById<Spinner>(R.id.brand_spinner)
        if(category.equals("mobile") || category_selected.equals("mobile"))
        {
            ArrayAdapter.createFromResource(
                requireContext(),
                    R.array.mobile_brand,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                brand_category = 0
                spinner.adapter = adapter

                spinner.onItemSelectedListener = this
            }
        }
        else if(category.equals("vehicle") || category_selected.equals("vehicle"))
        {
            ArrayAdapter.createFromResource(
                requireContext(),
                    R.array.vehicle_brand,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                brand_category = 1
                spinner.adapter = adapter

                spinner.onItemSelectedListener = this
            }
        }
        else if(category.equals("electronics") || category_selected.equals("electronics"))
        {
            ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.electronics_brand,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                brand_category = 2
                spinner.adapter = adapter
            }
        }
        else if(category.equals("furniture") || category_selected.equals("furniture"))
        {
            ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.furniture_brand,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                brand_category = 3
                spinner.adapter = adapter
            }
        }
        else if(category.equals("property") || category_selected.equals("property"))
        {
            ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.property_brand,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                brand_category = 4
                spinner.adapter = adapter
            }
        }
        else
        {
            ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.other_brand,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                brand_category = 5
                spinner.adapter = adapter
            }
        }

        adNextB.setOnClickListener {

            val title = titleET.text.toString()
            val desc = descET.text.toString()
            val price = priceET.text.toString()
            val bundle_ad = Bundle()
            val bundle = bundleOf("category" to category, "brand" to item, "title" to title, "desc" to desc, "price" to price)
            bundle_ad.putBundle("adDetails", bundle)
            if(title.isNotEmpty() && desc.isNotEmpty() && price.isNotEmpty() && itemSelected) {
                if(advert == null) {
                    findNavController().navigate(R.id.action_new_ad_details_to_imagePickerFragment, bundle_ad)
                }
                else{
                    advert?.category = category_selected
                    advert?.title = title
                    advert?.description = desc
                    advert?.price = price
                    advert?.brand = item
                    val imgFrag = ImagePickerFragment.newInstance(advert!!, "yes")
                    activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment, imgFrag)
                            ?.addToBackStack(null)
                            ?.commit()
                }
            }
            else if(title.isEmpty())
                titleET.error = "Enter the title of Ad"
            else if(desc.isEmpty())
                descET.error = "Enter the description of Ad"
            else if(price.isEmpty())
                priceET.error = "Enter the price"
            else
                Toast.makeText(activity, "Select a brand", Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment new_ad_details.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Advertisement, category: String) =
            new_ad_details().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, category)
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(brand_category) {
            0 -> {
                val mobileArray = resources.getStringArray(R.array.mobile_brand)
                item = mobileArray[position]
            }
            1 -> {
                val vehicleArray = resources.getStringArray(R.array.vehicle_brand)
                item = vehicleArray[position]
            }
            2 -> {
                val electronicsArray = resources.getStringArray(R.array.electronics_brand)
                item = electronicsArray[position]
            }
            3 -> {
                val furnitureArray = resources.getStringArray(R.array.furniture_brand)
                item = furnitureArray[position]
            }
            4 -> {
                val propertyArray = resources.getStringArray(R.array.property_brand)
                item = propertyArray[position]
            }
            5 -> {
                val otherArray = resources.getStringArray(R.array.other_brand)
                item = otherArray[position]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        itemSelected = false
    }
}