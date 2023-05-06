package com.example.affanstore

data class User(
    val address: Address,
    val email: String,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
):java.io.Serializable{
    val id: Int=0
}