package ar.edu.unq.eperdemic.modelo


import javax.persistence.*


@Entity
class Mutacion() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
    //lateinit var tipoDeMutacion: TipoDeMutacion
    @ManyToOne
    lateinit var especie: Especie
    var tipoDeVector : TipoDeVector? = null
    var poderDeMutacion: Int? = null
}
/*
enum class TipoDeMutacion {
    Supresion_Biomecanica{
        val potenciaDeMutacion: Int = Random.decidir(100)
                         },
    Bioalteracion_Genetica{
        val tipoDeVector: TipoDeVector = Random.decidirTipo(3)
    }
    */




