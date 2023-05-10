package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.stereotype.Service

interface PatogenoService {
    fun crearPatogeno(patogeno: Patogeno): Patogeno
    fun recuperarPatogeno(id: Long): Patogeno
    fun recuperarATodosLosPatogenos(): List<Patogeno>
    fun agregarEspecie(id: Long, nombre: String, ubicacionId : Long) : Especie

    fun esPandemia (especieId: Long) : Boolean
    fun especiesDePatogeno(patogenoId: Long ): List<Especie>
}