package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.InfeccionReporte

interface InfeccionReporteService {

    fun agregarInfeccionReporte(idVectorInfectado: Long, idEspecie: Long): InfeccionReporte
    fun findAllByVectorId(idVectorInfectado: Long): List<InfeccionReporte>
}