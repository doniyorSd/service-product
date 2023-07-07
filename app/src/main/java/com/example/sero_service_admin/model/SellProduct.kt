package com.example.sero_service_admin.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SellProduct(
    var id: String? = null,
    var debt: Int? = null,
    var dateAndTime: String? = null,
    var soldUser: User? = null,
    var toCompany:String?= null,
    var paySum:Int?= null,
    var productList: ArrayList<Product>? = ArrayList()
) : java.io.Serializable