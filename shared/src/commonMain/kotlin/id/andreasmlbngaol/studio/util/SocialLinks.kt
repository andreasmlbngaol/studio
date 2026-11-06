package id.andreasmlbngaol.studio.util

object SocialLinks {
    const val name = "Andreas Manatar Lumban Gaol"
    const val email = "lgandre45@gmail.com"
    const val location = "Medan, Indonesia"
    const val github = "https://github.com/andreasmlbngaol"
    const val linkedin = "https://www.linkedin.com/in/andreas-manatar-lumban-gaol-a11a641a8/"
    const val instagram = "https://www.instagram.com/andreasmlbngaol_"

    fun mailto(subject: String = "", body: String = ""): String {
        val query = buildList {
            if (subject.isNotBlank()) add("subject=${encode(subject)}")
            if (body.isNotBlank()) add("body=${encode(body)}")
        }
        return if (query.isEmpty()) "mailto:$email" else "mailto:$email?${query.joinToString("&")}"
    }

    private fun encode(value: String): String = buildString {
        for (char in value) {
            when (char) {
                ' ' -> append("%20")
                '\n' -> append("%0A")
                '&' -> append("%26")
                '?' -> append("%3F")
                '=' -> append("%3D")
                '#' -> append("%23")
                else -> append(char)
            }
        }
    }
}
