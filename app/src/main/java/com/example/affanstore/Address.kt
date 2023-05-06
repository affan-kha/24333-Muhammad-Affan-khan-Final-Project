package com.example.affanstore

data class Address(
    val city: String,
    val geolocation: Geolocation,
    val number: Int,
    val street: String,
    val zipcode: String
):java.io.Serializable{

    override fun toString(): String {
        return "City: $city\nStreet: $street\nNumber: $number\nZipcode: $zipcode"
    }
}