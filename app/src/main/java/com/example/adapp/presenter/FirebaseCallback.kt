package com.example.adapp.presenter

import com.example.adapp.model.Response

interface FirebaseCallback {
    fun onResponse(response: Response)
}