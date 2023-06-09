package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.UbicacionNeo4J
import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UbicacionService {

    fun mover(vectorId: Long, ubicacionid: Long)
    fun expandir(ubicacionId: Long)
    fun moverMasCorto(vectorId:Long, nombreDeUbicacion:String)
    fun conectar(nombreUbicacion1: String, nombreUbicacion2: String, tipoCamino: String)
    fun conectados(ubicacionOrigen:String):List<Ubicacion>

    /* Operaciones CRUD*/
    fun crearUbicacion(nombreUbicacion: String): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun recuperarTodos(page: Pageable): Page<Ubicacion>
    fun recuperar(ubicacionId: Long): Ubicacion
    fun recuperarVectores(ubicacionId: Long): List<Vector>
    fun recuperarUbicacionPorNombre(nombreUbicacion: String): Ubicacion
    fun recuperarUbicacionNeoPorId(idUbicacion:Long) : UbicacionNeo4J
}