package ar.edu.unq.eperdemic.modelo

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.MapId
import org.springframework.data.cassandra.core.mapping.Table

@Table
class PruebaCassandra {

    @PrimaryKey
    var nombre: String? = null
    var cantidadDeInfectados: Int = 0
}