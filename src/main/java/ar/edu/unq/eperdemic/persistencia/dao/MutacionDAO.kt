package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Mutacion
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MutacionDAO: CrudRepository<Mutacion, Long> {
}