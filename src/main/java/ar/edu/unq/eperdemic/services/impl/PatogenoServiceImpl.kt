package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Diosito
import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.exceptions.NingunVectorAInfectarEnLaUbicacionDada
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
    private val vectorServiceImpl = VectorServiceImpl(vectorDAO)
    private val ubicacionServiceImpl = UbicacionServiceImpl(ubicacionDAO)

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return runTrx { patogenoDAO.crear(patogeno) }
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return runTrx { patogenoDAO.recuperarPatogeno(id) }
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return runTrx { patogenoDAO.recuperarATodos() }
    }

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {

        val ubicacion = ubicacionServiceImpl.recuperar(ubicacionId)
        val vectores = ubicacionServiceImpl.recuperarVectores(ubicacionId)
        val vectorAInfectar = try{
            vectores[diosito.decidir(vectores.size)-1]
        } catch (e: Exception){
            throw NingunVectorAInfectarEnLaUbicacionDada("No hay ningún vector en la ubicación dada")
        }
        val especie = runTrx {
            val patogeno = patogenoDAO.recuperarPatogeno(id)
            val especieGenerada = patogeno.crearEspecie(nombre, ubicacion.nombre)
            especieDAO.guardar(especieGenerada)
            especieGenerada
        }

        vectorServiceImpl.infectar(vectorAInfectar, especie)
        return especie
    }

    override fun esPandemia(especieId: Long): Boolean {
        return runTrx {
            val cantUbicaciones = ubicacionServiceImpl.recuperarTodos().size
            val vectoresConEspecieId = vectorServiceImpl.recuperarTodos().filter { v -> v.tieneEfermedad(especieId) }
            val ubicacionesDeVectoresEnfermosConEspecie = vectoresConEspecieId.map { v -> v.ubicacion}
            ubicacionesDeVectoresEnfermosConEspecie.distinctBy { it.nombre }
            val cantUbicacionesDeLaEspecie = ubicacionesDeVectoresEnfermosConEspecie.size

            cantUbicaciones/2 < cantUbicacionesDeLaEspecie
        }
    }

    override fun especiesDePatogeno(patogenoId: Long ): List<Especie> {
        return runTrx {  patogenoDAO.especiesDePatogeno(patogenoId) }
    }
}