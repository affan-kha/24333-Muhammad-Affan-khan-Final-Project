package com.example.affanstore

data class Cart(
    val userId: Int,
    val date: String,
    val products: List<CartProduct>,
):java.io.Serializable{
    val id: Int = 0
}