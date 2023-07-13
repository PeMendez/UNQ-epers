package ar.edu.unq.eperdemic.modelo

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.core.mapping.PrimaryKey

@Table
class InfeccionSegunEspecie(
    @Column
    var nombreEspecie: String,
    @Column
    var paisOrigenEspecie: String,
    @Column
    var tipoDeVectorInfectado: String,
    @Column
    var idVectorEnfermo: Long? = null) {

    @PrimaryKey
    val id: Long? = null
}