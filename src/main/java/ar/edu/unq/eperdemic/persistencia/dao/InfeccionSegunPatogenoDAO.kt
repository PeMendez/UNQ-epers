package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.InfeccionReporte
import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import org.springframework.data.cassandra.repository.AllowFiltering
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.data.cassandra.repository.Query
import java.util.*

interface InfeccionSegunPatogenoDAO: CassandraRepository<InfeccionSegunPatogeno, UUID> {

    @Query("a")
    fun findAllByVectorId(idVectorInfectado: Long): List<InfeccionSegunPatogeno>

}