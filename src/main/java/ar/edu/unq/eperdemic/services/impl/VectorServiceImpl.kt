package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class VectorServiceImpl(private val vectorDAO: VectorDAO): VectorService {

    val ubicacionDAO = HibernateUbicacionDAO()

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        return runTrx {vectorDAO.contagiar(vectorInfectado,vectores) }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        return runTrx { vectorDAO.infectar(vector,especie) }
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        return runTrx { vectorDAO.enfermedades(vectorId) }
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        return runTrx {
            val ubicacion = ubicacionDAO.recuperar(ubicacionId)
            vectorDAO.crearVector(tipo,ubicacion)
        }
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return runTrx { vectorDAO.recuperarVector(vectorId) }
    }

    override fun borrarVector(vectorId: Long) {
        return runTrx { vectorDAO.borrarVector(vectorId) }
    }
}