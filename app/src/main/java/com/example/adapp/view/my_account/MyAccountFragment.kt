package com.example.adapp.view.my_account

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.adapp.view.MainActivity
import com.example.adapp.R
import com.example.adapp.model.Image
import com.example.adapp.model.Response
import com.example.adapp.presenter.AuthPresenter
import com.example.adapp.presenter.FirebaseCallback
import com.example.adapp.presenter.MyAcountDataPresenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_my_account.*
import java.io.ByteArrayOutputStream
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAccountFragment : Fragment(),MyAcountDataPresenter.View,FirebaseCallback,AuthPresenter.View {
    // TODO: Rename and change types of parameters
    private val GALLERY : Int = 1
    private  val CAMERA : Int = 2
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myAcountDataPresenter: MyAcountDataPresenter
    lateinit var authPresenter: AuthPresenter
    var PREF_USER="AccountInfo"
    lateinit var img: Image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        img = Image()
        myAcountDataPresenter= MyAcountDataPresenter(this)
        myAcountDataPresenter.getAccountDetails(this)
        authPresenter=AuthPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        editDetailsB.setOnClickListener{
            editDetailsB.visibility=View.INVISIBLE
            accountUserNameET.isEnabled=true
            accountUserPhoneET.isEnabled=true
            cancelB.visibility=View.VISIBLE
            submitB.visibility=View.VISIBLE
            logoutB.visibility=View.INVISIBLE
            adDetailImg.isClickable = true
        }


        adDetailImg.setOnClickListener {
            requestMultiplePermissions()
            showPictureDialog()
        }

        logoutB.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))

        }
        cancelB.setOnClickListener{
            val pref=requireActivity().getSharedPreferences(PREF_USER,MODE_PRIVATE)
            val username=pref.getString("username","")
            val phoneNumber=pref.getString("phoneNumber","")
            accountUserNameET.setText(username)
            accountUserPhoneET.setText(phoneNumber)
            accountUserNameET.isEnabled=false
            accountUserPhoneET.isEnabled=false
            submitB.visibility=View.INVISIBLE
            cancelB.visibility=View.INVISIBLE
            logoutB.visibility=View.VISIBLE
            editDetailsB.visibility=View.VISIBLE




        }
        submitB.setOnClickListener{
            displayAlertDialog()
            accountUserPhoneET.isEnabled=false
            accountUserNameET.isEnabled=false
            submitB.visibility=View.INVISIBLE
            cancelB.visibility=View.INVISIBLE
            logoutB.visibility=View.VISIBLE
            editDetailsB.visibility=View.VISIBLE


        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun displayAlertDialog() {
        var builder= androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want make changes?")
        builder.setPositiveButton("No")  { dlg, i-> dlg.cancel()
        }
        builder.setNegativeButton("Yes") { dlg, i ->
            val changedUserName=accountUserNameET.text.toString()
            val changedPhoneNumber=accountUserPhoneET.text.toString()
           if(changedUserName.isEmpty())
           {
               accountUserNameET.setError("Username cannot be empty!")
           }
            if(changedPhoneNumber.isEmpty())
            {
                accountUserPhoneET.setError("Phone number cannot be empty")

            }
            if(changedPhoneNumber.length!=10)
            {
                accountUserPhoneET.setError("Enter a valid phone number!")
            }
            authPresenter.updateData(changedPhoneNumber, changedUserName, img.imgUri)
        }
        builder.create().show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun getFileExtension(uri: Uri): String? {
        val cr = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr?.getType(uri))
    }

    override fun onResponse(response: Response) {
        val userDetails=response.user
        if(userDetails!=null)
        {
            val pref=requireActivity().getSharedPreferences(PREF_USER,MODE_PRIVATE)
            val editor=pref.edit()
            editor.putString("username",userDetails.username)
            editor.putString("phoneNumber",userDetails.phoneNumber)
            editor.commit()
            accountUserEmailET.isEnabled=false
            accountUserNameET.isEnabled=false
            accountUserPhoneET.isEnabled=false

            adDetailImg.isClickable = false
            if(userDetails.imageUrl != null) {
                Glide.with(view?.context!!).load(Uri.parse(userDetails.imageUrl)).into(adDetailImg)
            }
            else{
                adDetailImg.setImageResource(R.drawable.user_icon)
            }

            accountUserPhoneET.setText(userDetails.phoneNumber)
            accountUserEmailET.setText(userDetails.email)
            accountUserNameET.setText(userDetails.username)
            accountPB.visibility=View.INVISIBLE
        }

    }

    private fun showPictureDialog() {

        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Action")
            .setItems(pictureDialogItems) { dialog, which ->
                // Respond to item chosen
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            }
            .show()
//
    }
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI: Uri? = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        context?.contentResolver,
                        contentURI
                    )
                    adDetailImg.setImageBitmap(bitmap)
                    img.imgUri = contentURI!!
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == CAMERA) {
            if (data != null) {
                val thumbnail = data.extras!!["data"] as Bitmap?
                adDetailImg.setImageBitmap(thumbnail)
                img.imgUri = getImageUri(requireContext(), thumbnail!!)
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun requestMultiplePermissions() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(
                            context,
                            "All permissions are granted by user!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener { Toast.makeText(context, "Some Error! ", Toast.LENGTH_SHORT).show() }
            .onSameThread()
            .check()
    }

}