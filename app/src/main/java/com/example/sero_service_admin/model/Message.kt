package com.example.sero_service_admin.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var id:String?= null,
    var fromUser:String?= null,
    var userName:String?= null,
    var message:String?= null,
    var dateAndTime:String?= null
)
