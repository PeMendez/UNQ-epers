package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion
//import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.repository.CrudRepository

interface UbicacionDAO: CrudRepository<Ubicacion, Long> {


    /* Operaciones CRUD
    fun crearUbicacion(ubicacion: Ubicacion): Ubicacion
    fun recuperarTodos(): List<Ubicacion>
    fun recuperarVectores(ubicacionId: Long) : List<Vector>
    fun recuperarUbicacionPorNombre(nombreUbicacion: String) : Ubicacion*/

}