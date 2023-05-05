package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import org.springframework.data.repository.CrudRepository

interface EspecieDAO : CrudRepository<Especie, Long>  {

    fun recuperarEspecie(id: Long): Especie
    fun cantidadDeInfectados(especieId: Long ): Int
    fun recuperarTodas(): List<Especie>
    fun especieLider(): Especie
    fun lideres(): List<Especie>
    fun especieLiderDeUbicacion(ubicacionId: Long) : Especie

}