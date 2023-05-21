package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface EspecieService {
    fun recuperarEspecie(id: Long): Especie
    fun cantidadDeInfectados(especieId: Long ): Int
    fun recuperarTodas(page: Pageable): Page<Especie>
    fun recuperarTodas(): List<Especie>
    fun especieLider(): Especie
    fun lideres(): List<Especie>
    fun especieLiderDeUbicacion(ubicacionId: Long) : Especie
    fun existeElNombreEnLaBase(nombre: String): Boolean

}