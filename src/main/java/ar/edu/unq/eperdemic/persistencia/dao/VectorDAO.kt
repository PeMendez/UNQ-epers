package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VectorDAO: JpaRepository<Vector, Long> {

    @Query("select e from Vector v inner join v.especies e where v.id = :vectorId")
    fun findEnfermedades(vectorId: Long): List<Especie>

    @Query("from Vector v where v.ubicacion.id = :ubicacionId ")
    fun findAllByUbicacionId(ubicacionId: Long): List<Vector>
}