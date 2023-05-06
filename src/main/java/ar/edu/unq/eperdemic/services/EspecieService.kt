package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Especie

interface EspecieService {
    fun recuperarEspecie(id: Long): Especie
    fun cantidadDeInfectados(especieId: Long ): Int
    fun recuperarTodas(): List<Especie>
    fun especieLider(): Especie
    fun lideres(): List<Especie>

}