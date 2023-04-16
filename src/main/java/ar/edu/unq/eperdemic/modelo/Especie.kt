package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Especie(@ManyToOne var patogeno: Patogeno,
              var nombre: String,
              var paisDeOrigen: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null

}




