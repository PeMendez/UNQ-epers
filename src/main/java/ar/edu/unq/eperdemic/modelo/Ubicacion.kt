package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
@Entity
class Ubicacion(@Column(unique=true) var nombre: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

}