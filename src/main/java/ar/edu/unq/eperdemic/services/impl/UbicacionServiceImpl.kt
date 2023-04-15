package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class UbicacionServiceImpl(val hibernateUbicacionDAO: UbicacionDAO): UbicacionService {

    override fun mover(vectorId: Long, ubicacionid: Long) {
        TODO("Not yet implemented")
    }

    override fun expandir(ubicacionId: Long) {
        TODO("Not yet implemented")
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
       return runTrx { hibernateUbicacionDAO.crearUbicacion(nombreUbicacion) }
    }

    override fun recuperarTodos(): List<Ubicacion> {
        TODO("Not yet implemented")
    }

    fun recuperar(ubicacionId: Long) : Ubicacion {
        return runTrx { hibernateUbicacionDAO.recuperar(ubicacionId)}
    }

    fun recuperarVectores(ubicacionId: Long): List<Vector> {
        return runTrx { hibernateUbicacionDAO.recuperarVectores(ubicacionId) }
    }
}