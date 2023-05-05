package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.NoResultException

@Service
@Transactional
class UbicacionServiceImpl(): UbicacionService {

    @Autowired private lateinit var UbicacionDAO: UbicacionDAO
    override fun mover(vectorId: Long, ubicacionid: Long) {
        TODO("Not yet implemented")
    }

    override fun expandir(ubicacionId: Long) {
        TODO("Not yet implemented")
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        TODO("Not yet implemented")
    }

    override fun recuperarTodos(): List<Ubicacion> {
        TODO("Not yet implemented")
    }

    /*override fun mover(vectorId: Long, ubicacionid: Long) {

        val vector = vectorServiceImpl.recuperarVector(vectorId)

        if (vector.ubicacion.id!! != ubicacionid) {
            val vectoresEnUbicacion = runTrx {
                val ubicacion = ubicacionDAO.recuperar(ubicacionid)
                vector.mover(ubicacion)
                ubicacionDAO.recuperarVectores(ubicacionid)
            }

            runTrx {
                hibernateVectorDAO.actualizar(vector)
                if (!vector.estaSano()) {
                    vectorServiceImpl.contagiar(vector, vectoresEnUbicacion)
                }
            }
        }
    }

    override fun expandir(ubicacionId: Long) {
        val ubicacion = runTrx { ubicacionDAO.recuperar(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos") }
        val vectores = runTrx { ubicacionDAO.recuperarVectores(ubicacion.id!!) }
        runTrx {
            val vectoresInfectados = vectores.filter {  v -> !v.estaSano() }
            if (vectoresInfectados.isNotEmpty()) {
                val vectorAlAzar = vectoresInfectados[Random.decidir(vectoresInfectados.size)-1]
                vectorServiceImpl.contagiar(vectorAlAzar, vectores)
            }
        }
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        try {
            runTrx { ubicacionDAO.recuperarUbicacionPorNombre(nombreUbicacion) }
        } catch (e: NoResultException) {
            val nuevaUbicacion = Ubicacion(nombreUbicacion)
            return runTrx { ubicacionDAO.crearUbicacion(nuevaUbicacion) }
        }
        throw NombreDeUbicacionRepetido("Ya existe una ubicacion con ese nombre.")
    }

    override fun recuperarTodos(): List<Ubicacion> {
        return runTrx { ubicacionDAO.recuperarTodos() }
    }

    fun recuperar(ubicacionId: Long) : Ubicacion {
        return runTrx {
            ubicacionDAO.recuperar(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
        }
    }

    fun recuperarVectores(ubicacionId: Long): List<Vector> {
        return runTrx { ubicacionDAO.recuperarVectores(ubicacionId) }
    }*/
}