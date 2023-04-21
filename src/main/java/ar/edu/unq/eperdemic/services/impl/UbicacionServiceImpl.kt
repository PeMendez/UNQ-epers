package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Diosito
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class UbicacionServiceImpl(val ubicacionDAO: HibernateUbicacionDAO): UbicacionService {

    val hibernateVectorDAO = HibernateVectorDAO()
    val vectorServiceImpl = VectorServiceImpl(hibernateVectorDAO)

    override fun mover(vectorId: Long, ubicacionid: Long) {

        val vector = vectorServiceImpl.recuperarVector(vectorId)

        val vectoresEnUbicacion = runTrx {
            val ubicacion = ubicacionDAO.recuperar(ubicacionid)
            vector.mover(ubicacion)
            hibernateVectorDAO.actualizar(vector)
            ubicacionDAO.recuperarVectores(ubicacionid)
        }

        if (!vector.estaSano()) {
            vectorServiceImpl.contagiar(vector, vectoresEnUbicacion)
        }
    }

    override fun expandir(ubicacionId: Long) {
        return runTrx { val vectores = ubicacionDAO.recuperarVectores(ubicacionId)
                        val vectoresInfectados = vectores.filter { v -> !v.estaSano()}
                        val vectorAlAzar = vectoresInfectados[Diosito.decidir(vectoresInfectados.size)-1]
                        vectorServiceImpl.contagiar(vectorAlAzar, vectores)
        }
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