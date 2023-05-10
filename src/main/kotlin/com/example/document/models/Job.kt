package com.example.document.models


data class Job(
    var name: String?,
    var phone: String?,
    var address: String?,
    var images: MutableList<String>?,
    var description: String?,
    var rating: Double?,
    var beginDate: String?,
    var endDate: String?,
    var type: Int,
    var assigned: Boolean,
    var materials: MutableList<Material>?,
    var employers: MutableList<Employer>?,
    var subTasks: MutableList<Job>?,
    var completedSubTasks: MutableList<Job>?,
    val id: Long? = null
) {
    constructor() : this(
        "", "", "",
        mutableListOf(), "", 0.0,
        "", "", 0,
        false, mutableListOf(),
        mutableListOf(), mutableListOf(), mutableListOf()) {
    }
}