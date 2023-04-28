package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

interface UbicacionDAO {


    /* Operaciones CRUD*/
    fun crearUbicacion(ubicacion: Ubicacion): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun recuperarPorNombre(nombreUbicacion: String): Ubicacion
    fun recuperarVectores(ubicacionId: Long) : List<Vector>
    fun recuperarUbicacionPorNombre(nombreUbicacion: String) : Ubicacion

}