package ar.edu.unq.eperdemic.modelo

import org.hibernate.id.UUIDGenerator
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Table
class InfeccionSegunPatogeno(
    @Column
    val capacidadDeBiomecanizacionPatogeno:Int,
    @Column
    val capacidadDeContagioPatogeno:Int,
    @Column
    val tipoDePatogeno:String,
    @Column
    val tipoDeVectorInfectado: TipoDeVector,
    @Column
    val idVectorEnfermo: Long? = null) {

    @PrimaryKey
    val id: UUID = UUID.randomUUID()
}