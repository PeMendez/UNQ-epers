package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
@Repository
interface PatogenoDAO: CrudRepository<Patogeno, Long> {

    /*
    @Query("select p.especies from Patogeno p where p.id = :patogenoId")
     fun findAllEspeciesById(patogenoId: Long): List<Especie>
     @Query("select countdistinct(v.ubicacion) > ((select count(*) from Ubicacion ub) / 2) from Vector v join v.especies e  where e.id = :idBuscado")
     fun esPandemia(especieId: Long): Boolean
     */
}
