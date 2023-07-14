package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.InfeccionSegunEspecie
import org.springframework.data.cassandra.repository.CassandraRepository
import java.util.*

interface InfeccionSegunEspecieDAO: CassandraRepository<InfeccionSegunEspecie, UUID> {

}