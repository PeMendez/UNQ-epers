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
    private val vectorServiceImpl = VectorServiceImpl(vectorDAO)

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return runTrx { patogenoDAO.crear(patogeno) }
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return runTrx { patogenoDAO.recuperarPatogeno(id) }
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return runTrx { patogenoDAO.recuperarATodos() }
    }

    /* FALTA MANEJAR EL ERROR DE QUE NO HAYA VECTORES, CREO QUE HABRIA QUE HACERLO EN EL MODELO
    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {

        val ubicacion = ubicacionServiceImpl.recuperar(ubicacionId)
        val vectores = ubicacionServiceImpl.recuperarVectores(ubicacionId)
        val vectorAInfectar = vectores[diosito.decidir(vectores.size-1)]
        val especie = runTrx {
            val patogeno = patogenoDAO.recuperarPatogeno(id)
            val especie = patogeno.crearEspecie(nombre, ubicacion.nombre)
            hibernateEspecieDAO.guardar(especie)
            especie
        }

        vectorServiceImpl.infectar(vectorAInfectar, especie)
        return especie
    }
    */
    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        return runTrx {
            val patogeno = patogenoDAO.recuperarPatogeno(id)
            val ubicacion = ubicacionDAO.recuperar(ubicacionId)
            val especie = patogeno.crearEspecie(nombre, ubicacion.nombre)
            try {
                val vectores = ubicacionDAO.recuperarVectores(ubicacionId)
                val vectorAInfectar = vectores[diosito.decidir(vectores.size-1)]
                vectorServiceImpl.infectar(vectorAInfectar, especie)
            } catch (e: Exception){
                throw Exception("no hay ningún vector en la ubicación dada")
            }
            especieDAO.guardar(especie)
            especie
        }

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