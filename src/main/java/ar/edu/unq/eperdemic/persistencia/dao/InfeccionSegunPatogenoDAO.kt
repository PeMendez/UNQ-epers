package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import org.springframework.data.cassandra.repository.CassandraRepository
import java.util.*

interface InfeccionSegunPatogenoDAO: CassandraRepository<InfeccionSegunPatogeno, UUID> {


}