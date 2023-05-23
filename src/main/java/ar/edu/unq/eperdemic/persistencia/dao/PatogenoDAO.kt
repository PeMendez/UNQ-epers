package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
@Repository
interface PatogenoDAO: PagingAndSortingRepository<Patogeno, Long> {


     @Query("select p.especies from Patogeno p where p.id = :patogenoId")
     fun findAllEspeciesById(patogenoId: Long): List<Especie>
     @Query("select count(distinct v.ubicacion) > ((select count(*) from Ubicacion ub) / 2) from Vector v join v.especies e  where e.id = :especieId")
     fun esPandemia(especieId: Long): Boolean

}
