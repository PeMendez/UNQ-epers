package ar.edu.unq.eperdemic.modelo

object Check {

    fun validar(str: String): Boolean {
        val pattern = Regex("^[a-zA-Z0-9]+$")
        return str.isNotEmpty() && pattern.matches(str)
    }
}