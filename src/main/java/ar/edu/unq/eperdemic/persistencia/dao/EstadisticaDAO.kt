package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios

interface EstadisticaDAO {

    fun especieLider() : Especie

    fun lideres() : List<Especie>

    fun reporteDeContagios(nombreDeLaUbicacion:String) : ReporteDeContagios


}