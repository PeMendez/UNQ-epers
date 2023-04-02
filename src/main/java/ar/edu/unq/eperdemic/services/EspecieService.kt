package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios

interface EspecieService {
    fun recuperarEspecie(id: Long): Especie
}