package com.example.wellfoundrecruiting
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    var author: String? = "",
    var benefits: List<String>? = emptyList(),
    var company: String? = "",
    var description: String? = "",
    var location: String? = "",
    var requirements: List<String>? = emptyList(),
    var salary: String? = "",
    var timestamp: Long? = 0,
    var title: String? = ""
) {
    constructor() : this("", emptyList(), "", "", "", emptyList(), "", 0, "")
}

