package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class PatogenoServiceImpl(val hibernatePatogenoDAO: PatogenoDAO, val hibernateUbicacionDAO: UbicacionDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return runTrx { hibernatePatogenoDAO.crear(patogeno) }
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return runTrx { hibernatePatogenoDAO.recuperar(id) }
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return runTrx { hibernatePatogenoDAO.recuperarATodos() }
    }

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        val patogeno = hibernatePatogenoDAO.recuperar(id)
        val ubicacionNombre = hibernateUbicacionDAO.recuperar(ubicacionId).nombre
        return patogeno.crearEspecie(nombre, ubicacionNombre)
        //return runTrx { hibernatePatogenoDAO.agregarEspecie(id, nombre, ubicacionId) }
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        TODO("Not yet implemented")
    }

    override fun esPandemia(especieId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun especiesDePatogeno(patogenoId: Long ): List<Especie> {
        return runTrx {  hibernatePatogenoDAO.especiesDePatogeno(patogenoId) }
    }
}