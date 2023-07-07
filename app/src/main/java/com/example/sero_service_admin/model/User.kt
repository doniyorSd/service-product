package com.example.sero_service_admin.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String? = null,
    var login: String? = null,
    var name: String? = null,
    var password: String? = null,
    var position: UserEnum? = null,
    var phoneNumber: String? = null,
) : java.io.Serializable
