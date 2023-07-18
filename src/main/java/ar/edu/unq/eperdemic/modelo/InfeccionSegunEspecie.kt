package ar.edu.unq.eperdemic.modelo

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.repository.AllowFiltering
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id


@Table
class InfeccionSegunEspecie(
    @Column
    var idVectorInfectado: Long,
    @Column
    var nombreEspecie: String,
    @Column
    var paisOrigenEspecie: String,
    @Column
    var tipoDeVectorInfectado: TipoDeVector) {

    @PrimaryKey
    var id: UUID = UUID.randomUUID()
}