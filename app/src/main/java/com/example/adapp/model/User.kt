package com.example.adapp.model

import java.lang.Exception

data class User(val username:String? = null,
                val email:String? = null,
                val password:String? = null,
                val phoneNumber:String? = null,
                val imageUrl: String? = null)

data class Response(var user: User? = null, var exception: Exception? = null)