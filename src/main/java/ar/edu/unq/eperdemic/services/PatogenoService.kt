package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PatogenoService {
    fun crearPatogeno(patogeno: Patogeno): Patogeno
    fun recuperarPatogeno(id: Long): Patogeno
    fun recuperarATodosLosPatogenos(): List<Patogeno>
    fun recuperarATodosLosPatogenos(page: Pageable): Page<Patogeno>
    fun agregarEspecie(id: Long, nombre: String, ubicacionId : Long) : Especie

    fun esPandemia (especieId: Long) : Boolean
    fun especiesDePatogeno(patogenoId: Long ): List<Especie>
}