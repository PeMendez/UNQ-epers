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
    var potenciaDeMutacion: Int? = null


    fun addEspecie(especie: Especie) {
        this.especie = especie
    }

    fun haceMagia(vector: Vector) {
        val especiesAEliminar = mutableListOf<Especie>()
        vector.especies.forEach { e ->
            if (e.capacidadDeDefensa() < this.potenciaDeMutacion!! && e.id!! != this.especie.id!!) {
                especiesAEliminar.add(e)
            }
        }
        especiesAEliminar.forEach { e -> vector.especies.remove(e) }
    }

    constructor(tipoDeMutacion: TipoDeMutacion): this(){
        this.tipoDeMutacion = tipoDeMutacion
        if(tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
            potenciaDeMutacion = Random.decidir(100)
        } else {
            tipoDeVector = Random.decidirTipoVector(2)
        }
    }

}

enum class TipoDeMutacion {
    SupresionBiomecanica, BioalteracionGenetica
}





