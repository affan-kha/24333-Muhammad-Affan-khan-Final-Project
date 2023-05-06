package com.example.affanstore

data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: String,
    val title: String
):java.io.Serializable{
    var quantity:Int = 0
}