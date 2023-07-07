package com.example.sero_service_admin.model

data class Company(
    var id: Int,
    var companyNAme: String,
    var generalDebt: Int,
    var sellProducts: List<SellProduct>
)
