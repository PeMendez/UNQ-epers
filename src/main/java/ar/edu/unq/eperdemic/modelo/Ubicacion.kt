package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
@Entity
class Ubicacion(@Column(unique=true) var nombre: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @OneToMany(mappedBy = "ubicacion", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var vectores : MutableList<Vector> = mutableListOf()

}