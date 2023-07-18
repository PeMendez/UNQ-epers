package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class InfeccionReporte(val idVectorInfectado: Long, val idEspecie: Long) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null
}