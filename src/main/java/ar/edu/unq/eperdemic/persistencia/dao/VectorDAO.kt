package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface VectorDAO: CrudRepository<Vector, Long> {

    @Query(
         "from Vector v where v.ubicacion.id = :ubicacionId "
    )
    fun findAllByUbicacionId(ubicacionId: Long): List<Vector>
    /*fun enfermedades(vectorID: Long) : List<Especie>
    fun crearVector(vector: Vector): Vector
    fun recuperarVector(vectorId: Long): Vector
    fun recuperarTodos() : List<Vector>
    fun guardar(entity : Vector)
    fun actualizar(vector: Vector)
    fun borrar(vector: Vector)*/
}