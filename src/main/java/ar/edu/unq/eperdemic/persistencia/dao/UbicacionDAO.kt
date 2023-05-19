package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface UbicacionDAO: PagingAndSortingRepository<Ubicacion, Long> {

    @Query(
        """ SELECT v 
            FROM Vector v
            WHERE v.ubicacion.id = :ubicacionId
        """
    )
    fun recuperarVectores(ubicacionId: Long) : List<Vector>

    @Query(
        """FROM Ubicacion u
           WHERE u.nombre = :nombreDeUbicacion
        """
    )
    fun recuperarUbicacionPorNombre(nombreDeUbicacion: String): Ubicacion

}