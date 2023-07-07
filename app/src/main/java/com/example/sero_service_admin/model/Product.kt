package com.example.sero_service_admin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Product(
    var id: String? = null,
    var name: String? = null,
    var productImg: String? = null,
    var price: Int = 0,
    var productCount: Int? = null,
    @get:Exclude var sellCount:Int?= null
) : Serializable
