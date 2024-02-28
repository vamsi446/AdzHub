package com.example.adapp.model

import android.net.Uri
import java.io.Serializable

data class Advertisement(var category: String? = null,
                         var brand: String? = null,
                         var title: String? = null,
                         var description: String? = null,
                         var price: String? = null,
                         var imageUrl: String? = null,
                         var uname: String? = null,
                         var contact: String? = null,
                         var location: String? = null,
                         var uid: String? = null,
                         var key: String? = null): Serializable

data class Image(var imgUri: Uri? = null): Serializable

data class Ad_response(var listOfAds: List<Advertisement>? = null)