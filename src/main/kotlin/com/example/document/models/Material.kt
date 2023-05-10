package com.example.document.models

data class Material(
    var name: String?,
    var count: Int?,
    var cost: Int?,
    val id: Long? = null
) {
    constructor() : this("", 0, 0) {

    }
}