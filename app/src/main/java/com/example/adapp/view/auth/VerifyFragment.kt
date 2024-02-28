package com.example.adapp.view.auth

import android.net.Uri
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.model.Advertisement
import com.example.adapp.model.Image
import com.example.adapp.model.Response
import com.example.adapp.view.my_ads.MyAdsFragment
import com.example.adapp.presenter.AddPresenter
import com.example.adapp.presenter.FirebaseCallback
import com.example.adapp.presenter.MyAcountDataPresenter
import kotlinx.android.synthetic.main.fragment_verify.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyFragment : Fragment(), FirebaseCallback, MyAcountDataPresenter.View, AddPresenter.View {
    val PREF_NAME = "currentLocation"
    lateinit var pref: SharedPreferences
    // TODO: Rename and change types of parameters
    private var imgUri: Image? = null
    private var ad: Advertisement? = null
    lateinit var accountPresenter: MyAcountDataPresenter
    var adBundle: Bundle? = null
    var img: Image? = null
    lateinit var addPresenter: AddPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            var check = it.getString(ARG_PARAM3)
            if(check != null) {
                imgUri = it.getSerializable(ARG_PARAM1) as Image
                ad = it.getSerializable(ARG_PARAM2) as Advertisement
            }

            if(ad == null) {
                adBundle = it.getBundle("adBundle")
                img = it.getSerializable("url") as Image
            }
        }
        accountPresenter = MyAcountDataPresenter(this)
        accountPresenter.getAccountDetails(this)
        addPresenter = AddPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pref= activity?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)!!
        val area  = pref.getString("area", "Enter Location:")
        locationVerifyET.setText(area)
        super.onViewCreated(view, savedInstanceState)

        postB.setOnClickListener {
            val name = usernameVerifyET.text.toString()
            val phone = contactVerifyET.text.toString()
            val loc = locationVerifyET.text.toString()
            if (adBundle != null) {
                val category = adBundle?.getString("category")
                val brand = adBundle?.getString("brand")
                val title = adBundle?.getString("title")
                val desc = adBundle?.getString("desc")
                val price = adBundle?.getString("price")
                val imgUrl = img?.imgUri?.toString()
                val ad = Advertisement(category, brand, title, desc, price, imgUrl, name, phone, loc)
                pBarVerify.visibility = View.VISIBLE
                addPresenter.addAd(ad, img?.imgUri!!)
                //Toast.makeText(activity, "$ad", Toast.LENGTH_LONG).show()
            }
            else{
                ad?.uname = name
                ad?.contact = phone
                ad?.location = loc

                if(imgUri?.imgUri != null){
                    pBarVerify.visibility = View.VISIBLE
                    addPresenter.updateAd(ad!!, imgUri?.imgUri!!)
                }
                else{
                    pBarVerify.visibility = View.VISIBLE
                    addPresenter.updateAdWithoutImage(ad!!)
                }
            }
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Image, param2: Advertisement, param3: String) =
            VerifyFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }

    override fun onResponse(response: Response) {
        usernameVerifyET.setText(response.user?.username)
        contactVerifyET.setText(response.user?.phoneNumber)
        pBarVerify.visibility = View.GONE
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun sentToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun getFileExtension(myUri: Uri): String? {
        val cr = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr?.getType(myUri))
    }

    override fun stopProgressBar() {
        pBarVerify.visibility = View.GONE
        val myAds = MyAdsFragment.newInstance(0)
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment, myAds)
                ?.commit()
    }
}