package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

interface UbicacionDAO {

    fun expandir(ubicacionId: Long)
    /* Operaciones CRUD*/
    fun crearUbicacion(nombreUbicacion: String): Ubicacion
    fun recuperarTodos(): List<Ubicacion>


    fun recuperarVectores(ubicacionId: Long) : List<Vector>

    fun recuperarUbicacionPorNombre(nombreUbicacion: String) : Ubicacion

}