package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class UbicacionServiceImpl(): UbicacionService {

    @Autowired private lateinit var ubicacionDAO: UbicacionDAO
    @Autowired private lateinit var vectorServiceImpl: VectorServiceImpl
    @Autowired private lateinit var vectorDAO: VectorDAO

    override fun mover(vectorId: Long, ubicacionid: Long) {
        val vector = vectorServiceImpl.recuperarVector(vectorId)
        if (vector.ubicacion.id!! != ubicacionid) {
            val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionid)?: throw NoExisteElid("el id de la ubiacion no existe en la base de datos")
            vector.mover(ubicacion)
            val vectoresEnUbicacion = ubicacionDAO.recuperarVectores(ubicacionid)
            vectorDAO.save(vector)
            if (!vector.estaSano()) {
                vectorServiceImpl.contagiar(vector, vectoresEnUbicacion)
            }
        }
    }

    override fun expandir(ubicacionId: Long) {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
        val vectores = ubicacionDAO.recuperarVectores(ubicacion.id!!)
        val vectoresInfectados = vectores.filter {  v -> !v.estaSano() }
        if (vectoresInfectados.isNotEmpty()) {
            val vectorAlAzar = vectoresInfectados[Random.decidir(vectoresInfectados.size)-1]
            vectorServiceImpl.contagiar(vectorAlAzar, vectores)
        }
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        try {
             ubicacionDAO.recuperarUbicacionPorNombre(nombreUbicacion)
        } catch (e: EmptyResultDataAccessException) {
            val nuevaUbicacion = Ubicacion(nombreUbicacion)
            return ubicacionDAO.save(nuevaUbicacion)
        }
        throw NombreDeUbicacionRepetido("Ya existe una ubicacion con ese nombre.")
    }

    override fun recuperarTodos(): List<Ubicacion> {
        return ubicacionDAO.findAll().toList()
    }

    override fun recuperarTodos(page: Pageable): Page<Ubicacion> {
        return ubicacionDAO.findAll(page)
    }


    override fun recuperar(ubicacionId: Long) : Ubicacion {
        return ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
    }

    override fun recuperarVectores(ubicacionId: Long): List<Vector> {
        return ubicacionDAO.recuperarVectores(ubicacionId)
    }

}

