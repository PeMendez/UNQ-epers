package ar.edu.unq.eperdemic.modelo

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import java.util.*

@Table
class InfeccionSegunEspecie(
    @Column
    val idVectorInfectado: Long,
    @Column
    var nombreEspecie: String,
    @Column
    var paisOrigenEspecie: String,
    @Column
    var tipoDeVectorInfectado: TipoDeVector) {

    @PrimaryKey
    val id: UUID = UUID.randomUUID()
}