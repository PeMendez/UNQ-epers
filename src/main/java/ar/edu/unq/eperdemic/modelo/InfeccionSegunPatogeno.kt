package ar.edu.unq.eperdemic.modelo

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table
class InfeccionSegunPatogeno(
    @Column
    val capacidadDeBiomecanizacionPatogeno:String,
    @Column
    val capacidadDeContagioPatogeno:String,
    @Column
    val tipoDePatogeno:String,
    @Column
    val tipoDeVectorInfectado: String,
    @Column
    val idVectorEnfermo: Long? = null) {

    @PrimaryKey
    val id: Long? = null
}