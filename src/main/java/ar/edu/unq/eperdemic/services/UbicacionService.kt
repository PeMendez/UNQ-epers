package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UbicacionService {

    fun mover(vectorId: Long, ubicacionid: Long)
    fun expandir(ubicacionId: Long)
    fun moverMasCorto(vectorId:Long, nombreDeUbicacion:String)

    /* Operaciones CRUD*/
    fun crearUbicacion(nombreUbicacion: String): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun recuperarTodos(page: Pageable): Page<Ubicacion>
    fun recuperar(ubicacionId: Long): Ubicacion
    fun recuperarVectores(ubicacionId: Long): List<Vector>
}