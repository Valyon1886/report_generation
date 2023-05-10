package com.example.document.models


data class Employer(
    var firstName: String,
    var secondName: String,
    var lastName: String,
    var post: String,
    var cost: Int,
    var clock: Int,
    val id: Long? = null
) {
    constructor() : this("", "", "", "", 0,0) {

    }
}