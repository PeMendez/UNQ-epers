package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class VectorServiceImpl(private val vectorDAO: VectorDAO): VectorService {

    val hibernateUbicacionDAO = HibernateUbicacionDAO()

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        runTrx{vectores.forEach { v ->
                intentarInfectarConEspeciesDeVector(v,vectorInfectado)
                vectorDAO.actualizar(v)
            }
        }
    }

    override fun intentarInfectarConEspeciesDeVector(vectorAInfectar: Vector, vectorInfectado: Vector) {
        vectorInfectado.especies.forEach { e ->
            vectorAInfectar.intentarInfectar(vectorInfectado, e)
        }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        runTrx {
            vector.infectarCon(especie)
            vectorDAO.actualizar(vector)
        }
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        return runTrx { vectorDAO.enfermedades(vectorId) }
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        return runTrx {
            val ubicacion = hibernateUbicacionDAO.recuperar(ubicacionId)
            vectorDAO.crearVector(tipo,ubicacion)
        }
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return runTrx { vectorDAO.recuperarVector(vectorId) }
    }

    override fun borrarVector(vectorId: Long) {
        return runTrx {
            val vectorABorrar = vectorDAO.recuperarVector(vectorId)
            vectorDAO.borrar(vectorABorrar) }
    }

    override fun recuperarTodos(): List<Vector> {
        return runTrx { vectorDAO.recuperarTodos() }
    }
}