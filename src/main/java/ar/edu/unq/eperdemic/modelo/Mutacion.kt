package ar.edu.unq.eperdemic.modelo


import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import javax.persistence.*


@Entity
class Mutacion() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
    lateinit var tipoDeMutacion: TipoDeMutacion
    @ManyToOne
    lateinit var especie: Especie

    final var tipoDeVector : TipoDeVector? = null
    final var potenciaDeMutacion: Int? = null

    init {
        tipoDeMutacion = Random.decidirTipoMutacion(0)
        if(tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
            potenciaDeMutacion = Random.decidir(100)
        } else {
            tipoDeVector = Random.decidirTipoVector(2)
        }
    }

    fun addEspecie(especie: Especie) {
        this.especie = especie
    }

}

enum class TipoDeMutacion {
    SupresionBiomecanica, BioalteracionGenetica
}





