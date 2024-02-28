package com.example.adapp.presenter

import com.example.adapp.model.Ad_response

interface RetrieveAdsCallback {
    fun onResponse(response: Ad_response)
}