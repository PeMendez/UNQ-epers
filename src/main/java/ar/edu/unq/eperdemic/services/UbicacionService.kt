package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

interface UbicacionService {

    fun mover(vectorId: Long, ubicacionid: Long)
    fun expandir(ubicacionId: Long)

    /* Operaciones CRUD*/
    fun crearUbicacion(nombreUbicacion: String): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun recuperar(ubicacionId: Long): Ubicacion
    fun recuperarVectores(ubicacionId: Long): List<Vector>
}