package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.InfeccionReporte
import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import org.springframework.data.cassandra.repository.AllowFiltering
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.data.cassandra.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InfeccionSegunPatogenoDAO: CassandraRepository<InfeccionSegunPatogeno, UUID> {

}