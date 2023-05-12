package ar.edu.unq.eperdemic.modelo.exceptions

import java.lang.Exception

class NoPuedeEstarVacioOContenerCaracteresEspeciales(override val message: String?) : Exception()