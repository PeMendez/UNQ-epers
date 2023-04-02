package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno

interface PatogenoService {
    fun crearPatogeno(patogeno: Patogeno): Patogeno
    fun recuperarPatogeno(id: Long): Patogeno
    fun recuperarATodosLosPatogenos(): List<Patogeno>
    fun agregarEspecie(id: Long, nombre: String, ubicacionId : Long) : Especie

    fun cantidadDeInfectados (especieId: Long) : Int
    fun esPandemia (especieId: Long) : Boolean
    fun recuperarEspecie(id: Long): Especie
}