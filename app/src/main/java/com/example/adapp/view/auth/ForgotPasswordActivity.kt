package com.example.adapp.view.auth

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.presenter.AuthPresenter
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity(),AuthPresenter.View{
    lateinit var authPresenter:AuthPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        authPresenter=AuthPresenter(this)

    }

    fun resetPasswordClick(view: View) {
        val email=resetEmailET.text.toString().trim()
        if(email.isEmpty())
        {
            resetEmailET.setError("Email should not be Empty")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            resetEmailET.setError("Email should not be Empty")
            resetEmailET.requestFocus()
        }
        else
        {
            authPresenter.resetPassword(email)
        }

    }

    override fun sendToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun getFileExtension(uri: Uri): String? {
        return null
    }
}