package com.example.adapp.view.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adapp.R
import com.example.adapp.model.Image
import com.example.adapp.presenter.AuthPresenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_image_picker.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.ByteArrayOutputStream
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class RegisterFragment : Fragment(),AuthPresenter.View {
    // TODO: Rename and change types of parameters
    private val GALLERY : Int = 1
    private  val CAMERA : Int = 2
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img: Image
    lateinit var regPresenter:AuthPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        regPresenter= AuthPresenter(this)
        img = Image()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        uploadProfilePicture.setOnClickListener {
            requestMultiplePermissions()
            showPictureDialog()
        }
        registerB.setOnClickListener {
            val username = usernameRegisterET.text.toString()
            val email = emailRegisterET.text.toString()
            val password = passwordRegisterET.text.toString()
            val confirmPassword = rePasswordRegisterET.text.toString()
            val phoneNo = phoneRegisterET.text.toString()
            val isMailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if(username.isNotEmpty() && email.isNotEmpty()
                && password.isNotEmpty() && confirmPassword.isNotEmpty()
                &&phoneNo.isNotEmpty())
            {
                if(password != confirmPassword)
                {
                    Toast.makeText(requireActivity(),"Password and confirm Password Doesn't match!",Toast.LENGTH_SHORT).show()
                }
                else if(password.length < 8)
                {
                    passwordRegisterET.setError("Password should have minimum 8 characters")
                }
                else if(!isMailValid)
                {
                    emailRegisterET.setError("Enter a valid Email!")
                }
                else if(phoneNo.length != 10)
                {
                    phoneRegisterET.setError("Enter a valid Phone Number!")
                }
                else
                {
                    var isCreated = false
                    if(img.imgUri != null) {
                        isCreated = regPresenter.createAccount(username, email, password, phoneNo, img.imgUri!!)
                    }
                    else{
                        img.imgUri = null
                        isCreated = regPresenter.createAccount(username, email, password, phoneNo, img.imgUri)
                    }
                    if(isCreated)
                    {
                        Toast.makeText(activity,"Registration Done Successfully.. login to continue", Toast.LENGTH_SHORT).show()
                        val loginFrag= SignInFragment()
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.parentL,loginFrag)
                            .commit()
                    }
                    else
                    {
                        Toast.makeText(activity,"Registration Failed", Toast.LENGTH_SHORT).show()

                    }
                }
            }
            else
            {
                Toast.makeText(requireActivity(),"Enter all the fields!",Toast.LENGTH_SHORT).show()
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun getFileExtension(uri: Uri) : String?{
        val cr = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr?.getType(uri))
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
                    uploadProfilePicture.setImageBitmap(bitmap)
                    img.imgUri = contentURI!!
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == CAMERA) {
            if (data != null) {
                val thumbnail = data.extras!!["data"] as Bitmap?
                uploadProfilePicture.setImageBitmap(thumbnail)
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