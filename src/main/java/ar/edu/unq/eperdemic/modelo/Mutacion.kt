package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

class Mutacion() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
    lateinit var tipoDeMutacion: TipoDeMutacion
    @ManyToOne
    var especieId: Long? = null
    lateinit var tipoDeVector : TipoDeVector
    var poderDeMutacion: Int =  Random.decidir(100)
}

enum class TipoDeMutacion {
    Supresion_Biomecanica, Bioalteracion_Genetica,
}

