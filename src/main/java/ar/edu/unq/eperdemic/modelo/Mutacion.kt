package ar.edu.unq.eperdemic.modelo

import javax.persistence.*


@Entity
class Mutacion() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
    lateinit var tipoDeMutacion: TipoDeMutacion
    @ManyToOne
    lateinit var especie: Especie
    lateinit var tipoDeVector : TipoDeVector
    var poderDeMutacion: Int =  Random.decidir(100)
}

enum class TipoDeMutacion {
    Supresion_Biomecanica, Bioalteracion_Genetica,

}

