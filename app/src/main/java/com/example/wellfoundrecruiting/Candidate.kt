import java.io.Serializable

data class Candidate(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    var photo_url: String = "",
    val city: String = "",
    val educationHistory: EducationHistory = EducationHistory()
) : Serializable {
    constructor() : this("", "", "", "", "", EducationHistory())
}

data class EducationHistory(
    val degree: String = "",
    val university: String = "",
    val graduationYear: Int = 0
) : Serializable
