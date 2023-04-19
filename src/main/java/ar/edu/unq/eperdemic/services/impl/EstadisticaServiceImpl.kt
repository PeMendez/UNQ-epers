package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EstadisticaServiceImpl() : EstadisticaService {

    val especieDAO = HibernateEspecieDAO()
    val ubicacionDAO = HibernateUbicacionDAO()

    override fun especieLider(): Especie {
        return runTrx { especieDAO.especieLider() }
    }

    override fun lideres(): List<Especie> {
        return runTrx { especieDAO.lideres() }
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        return runTrx { ubicacionDAO.reporteDeContagios(nombreDeLaUbicacion) }
    }


}