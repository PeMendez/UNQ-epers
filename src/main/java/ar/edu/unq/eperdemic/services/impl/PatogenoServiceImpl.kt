package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.hibernate.annotations.NotFound

class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return runTrx { patogenoDAO.crear(patogeno) }
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return runTrx { patogenoDAO.recuperar(id) }
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return runTrx { patogenoDAO.recuperarATodos() }
    }

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        val ubicacionDAO = HibernateUbicacionDAO()
        val vectorDAO = HibernateVectorDAO()
        return runTrx {
            val patogeno = patogenoDAO.recuperar(id)
            val ubicacion = ubicacionDAO.recuperar(ubicacionId)
            val especie = patogeno.crearEspecie(nombre, ubicacion.nombre)
            val especieDAO = HibernateEspecieDAO()
            try {
                val vectorAInfectar = ubicacionDAO.recuperarVectores(ubicacionId).random()
                vectorDAO.infectar(vectorAInfectar, especie)
            } catch (e: Exception){
                throw Exception("no hay ningún vector en la ubicación dada")
            }
            especieDAO.guardar(especie)
            especie
        }

    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        TODO("Not yet implemented")
    }

    override fun esPandemia(especieId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun especiesDePatogeno(patogenoId: Long ): List<Especie> {
        return runTrx {  patogenoDAO.especiesDePatogeno(patogenoId) }
    }
}