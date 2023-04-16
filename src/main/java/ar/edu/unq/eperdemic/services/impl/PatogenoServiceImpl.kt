package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx


class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    private val ubicacionDAO = HibernateUbicacionDAO()
    private val vectorDAO = HibernateVectorDAO()
    private val especieDAO = HibernateEspecieDAO()
    private val diosito = Diosito

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
        diosito.switchModo(true)
        return runTrx {
            val patogeno = patogenoDAO.recuperar(id)
            val ubicacion = ubicacionDAO.recuperar(ubicacionId)
            val especie = patogeno.crearEspecie(nombre, ubicacion.nombre)
            try {
                val vectores = ubicacionDAO.recuperarVectores(ubicacionId)
                val vectorAInfectar = vectores[diosito.decidir(vectores.size-1)]
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
        return runTrx {
            val cantUbicaciones = ubicacionDAO.recuperarTodos().size
            val vectoresConEspecieId = vectorDAO.recuperarTodos().filter { v -> v.tieneEfermedad(especieId) }
            var ubicacionesDeVectoresEnfermosConEspecie = vectoresConEspecieId.map { v -> v.ubicacion}
            ubicacionesDeVectoresEnfermosConEspecie.distinctBy { it.nombre }
            val cantUbicacionesDeLaEspecie = ubicacionesDeVectoresEnfermosConEspecie.size

            cantUbicaciones/2 < cantUbicacionesDeLaEspecie
        }
    }

    override fun especiesDePatogeno(patogenoId: Long ): List<Especie> {
        return runTrx {  patogenoDAO.especiesDePatogeno(patogenoId) }
    }
}