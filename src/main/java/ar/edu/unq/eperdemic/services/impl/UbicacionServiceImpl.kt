package ar.edu.unq.eperdemic.services.impl

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

        val ubicacion =  runTrx {
            val ubicacion = ubicacionDAO.recuperar(ubicacionid)
            vector.mover(ubicacion)
            ubicacion.vectores.add(vector)
            // habria que borrar al vector de la lista de vectores de la ubicacion vieja?
            // y actualizar la ubicacion en el vector?
            ubicacionDAO.actualizar(ubicacion)
            ubicacion
        }

        if (vector.especies.isNotEmpty()) {
            vectorServiceImpl.contagiar(vector, ubicacion.vectores)
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