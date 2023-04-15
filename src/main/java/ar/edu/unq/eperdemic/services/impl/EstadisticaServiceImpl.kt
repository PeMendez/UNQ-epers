package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EstadisticaServiceImpl(val estadisticaDAO : EstadisticaDAO) : EstadisticaService {


    override fun especieLider(): Especie {
        return runTrx { estadisticaDAO.especieLider() }
    }

    override fun lideres(): List<Especie> {
        return runTrx { estadisticaDAO.lideres() }
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        return runTrx { estadisticaDAO.reporteDeContagios(nombreDeLaUbicacion) }
    }


}