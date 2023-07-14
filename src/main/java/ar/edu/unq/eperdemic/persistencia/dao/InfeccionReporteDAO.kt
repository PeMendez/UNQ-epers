package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.InfeccionReporte
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface InfeccionReporteDAO : CrudRepository<InfeccionReporte, Long> {

    @Query("""
        FROM InfeccionReporte i
        WHERE i.idVectorInfectado = :idVectorInfectado
    """)
    fun findAllByVectorId(idVectorInfectado: Long): List<InfeccionReporte>
}