package com.example.wellfoundrecruiting

data class Candidate(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val photo_url: String = "",
    val city: String = "",
    val educationHistory: EducationHistory = EducationHistory()
) {
    constructor() : this("", "", "", "", "", EducationHistory())
}

data class EducationHistory(
    val degree: String = "",
    val university: String = "",
    val graduationYear: Int = 0
)
