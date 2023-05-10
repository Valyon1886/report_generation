package com.example.document.models

import com.example.document.models.Employer
import com.example.document.models.Job


data class User(
    var idToken: String?,
//    @Column(nullable = true) var password: String,
    var firstName: String?,
    var secondName: String?,
    var lastName: String?,
    var imgSrc: String?,

    var completedTasks: MutableList<Job>?,
    //val documents: Map<Date, String>

    var jobs: MutableList<Job>?,

    var employers: MutableList<Employer>?,
    val id: Long? = null
) {
    constructor() : this("", "", "", "", "", mutableListOf(), mutableListOf(), mutableListOf()) {

    }
}