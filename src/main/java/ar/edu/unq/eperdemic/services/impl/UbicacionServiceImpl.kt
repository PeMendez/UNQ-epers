package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class UbicacionServiceImpl(val ubicacionDAO: HibernateUbicacionDAO): UbicacionService {

    val vectorDAO = HibernateVectorDAO()
    val vectorService = VectorServiceImpl(vectorDAO)

    override fun mover(vectorId: Long, ubicacionid: Long) {
        runTrx {
            val ubicacion = ubicacionDAO.recuperar(ubicacionid)
            val vector = vectorService.recuperarVector(vectorId)

            vector.mover(ubicacion)

            if (vector.especies.isNotEmpty()) {
                vectorService.contagiar(vector, ubicacion.vectores)
            }

            ubicacion.vectores.add(vector)
            ubicacionDAO.actualizar(ubicacion)
        }
    }

    override fun expandir(ubicacionId: Long) {
        TODO("Not yet implemented")
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        return runTrx { ubicacionDAO.crearUbicacion(nombreUbicacion) }
    }

    override fun recuperarTodos(): List<Ubicacion> {
        return runTrx { ubicacionDAO.recuperarTodos() }
    }

    fun recuperar(ubicacionId: Long) : Ubicacion {
        return runTrx { ubicacionDAO.recuperar(ubicacionId)}
    }

    fun recuperarVectores(ubicacionId: Long): List<Vector> {
        return runTrx { ubicacionDAO.recuperarVectores(ubicacionId) }
    }
}