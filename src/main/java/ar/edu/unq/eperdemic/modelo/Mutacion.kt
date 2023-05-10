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
    var tipoDeVector : TipoDeVector? = null
    var poderDeMutacion: Int? = null


    fun addEspecie(especie: Especie) {
        this.especie = especie
    }
}

enum class TipoDeMutacion {
    SupresionBiomecanica{
        val potenciaDeMutacion: Int = Random.decidir(100)
                         },
    BioalteracionGenetica {
        val tipoDeVector: TipoDeVector = Random.decidirTipoVector(2)
    }
}





