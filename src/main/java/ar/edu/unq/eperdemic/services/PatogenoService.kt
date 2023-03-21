package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno

interface PatogenoService {
    fun crearPatogeno(patogeno: Patogeno): Patogeno
    fun recuperarPatogeno(id: Long): Patogeno
    fun recuperarATodosLosPatogenos(): List<Patogeno>
    fun agregarEspecie(id: Long, nombre: String, paisDeOrigen : String) : Especie

}