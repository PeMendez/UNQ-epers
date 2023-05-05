package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class VectorServiceImpl(): VectorService {

    @Autowired
    private lateinit var vectorDAO: VectorDAO
    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TODO("Not yet implemented")
    }

    override fun intentarInfectarConEspeciesDeVector(vectorAInfectar: Vector, vectorInfectado: Vector) {
        TODO("Not yet implemented")
    }

    override fun infectar(vector: Vector, especie: Especie) {
        TODO("Not yet implemented")
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        TODO("Not yet implemented")
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun recuperarVector(vectorId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun borrarVector(vectorId: Long) {
        TODO("Not yet implemented")
    }

    override fun recuperarTodos(): List<Vector> {
        TODO("Not yet implemented")
    }
    /*val hibernateUbicacionDAO = HibernateUbicacionDAO()
    val especieDAO = HibernateEspecieDAO()

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        try {
            runTrx { vectorDAO.recuperarVector(vectorInfectado.id!!)?: throw NoExisteElid("El ID de Vector dado no existe") }
        } catch (e: Exception) {
            throw NoExisteElid("El ID de Vector dado no existe")
        }
        runTrx { vectores.forEach { v ->
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
        try {
            runTrx { vectorDAO.recuperarVector(vector.id!!)?: throw NoExisteElid("El id de Vector ingresado no existe") }
        } catch (e: Exception) {
            throw NoExisteElid("El ID del Vector dado no es válido.")
        }
        try {
            runTrx { especieDAO.recuperarEspecie(especie.id!!)?: throw NoExisteElid("El id de Especie ingresado no existe") }
        } catch (e: Exception) {
            throw NoExisteElid("El ID de la Especie dada no es válido.")
        }
        runTrx {
            vector.infectarCon(especie)
            vectorDAO.actualizar(vector)
        }
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        return runTrx {
            vectorDAO.recuperarVector(vectorId)?: throw NoExisteElid("El id de Vector ingresado no existe")
            vectorDAO.enfermedades(vectorId)
        }
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        return runTrx {
            val ubicacion = hibernateUbicacionDAO.recuperar(ubicacionId)?: throw NoExisteElid("El id de Ubicacion ingresado no existe")
            val vector = Vector(tipo, ubicacion)
            vectorDAO.crearVector(vector)
        }
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return runTrx {
            vectorDAO.recuperarVector(vectorId)?: throw NoExisteElid("El id ingresado no existe")
        }
    }

    override fun borrarVector(vectorId: Long) {
        return runTrx {
            val vectorABorrar = vectorDAO.recuperarVector(vectorId)?: throw NoExisteElid("El id ingresado no existe")
            vectorDAO.borrar(vectorABorrar) }
    }

    override fun recuperarTodos(): List<Vector> {
        return runTrx { vectorDAO.recuperarTodos() }
    }*/
}