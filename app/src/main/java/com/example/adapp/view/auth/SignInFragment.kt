package com.example.adapp.view.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.presenter.AuthPresenter
import com.example.adapp.view.Nav_activity
import kotlinx.android.synthetic.main.fragment_sign_in.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment(),AuthPresenter.View {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var signinPresenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        signinPresenter = AuthPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.setOnClickListener{
            val userMail=usernameLoginET.text.toString()
            val userPassword=passwordLoginET.text.toString()
            if(userMail.isNotEmpty() && userPassword.isNotEmpty())
            {
                val isMailValid=android.util.Patterns.EMAIL_ADDRESS.matcher(userMail).matches()

                if(!isMailValid)
                {
                    usernameLoginET.setError("Enter a valid Email!")
                }
                else if(userPassword.length<8)
                {
                    passwordLoginET.setError("Password should have minimum 8 characters!")
                }
                else
                {
                    val isSignedIn=signinPresenter.loginUser(userMail,userPassword)
                    if(isSignedIn)
                    {
                        startActivity(Intent(requireContext(), Nav_activity::class.java))
                    }
                }
            }
            else
            {
                Toast.makeText(requireActivity(),"Enter all the credentials!",Toast.LENGTH_SHORT).show()
            }
        }

        newUser.setOnClickListener {
            val regFrag= RegisterFragment()
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.parentL,regFrag).commit()
        }
        forgotPasswordTV.setOnClickListener {
            startActivity(Intent(requireActivity(), ForgotPasswordActivity::class.java))
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
         * @return A new instance of fragment SignInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun getFileExtension(uri: Uri): String? {
        return null
    }
}