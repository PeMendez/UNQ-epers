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

    fun activarSupresionSiCorresponde(vector: Vector){
        if (tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
           activarSupresion(vector)
        }
    }

    fun esBioalteracionGenetica(): Boolean{
        return tipoDeMutacion == TipoDeMutacion.BioalteracionGenetica
    }

    fun activarSupresion(vector: Vector) {
        val especiesAEliminar = mutableListOf<Especie>()
        vector.especies.forEach { e ->
            if (e.capacidadDeDefensa() < this.potenciaDeMutacion!! && !e.sonMismaEspecie(this.especie)) {
                especiesAEliminar.add(e)
            }
        }
        especiesAEliminar.forEach { e -> vector.especies.remove(e) }
    }

    fun sonMismaMutacion(mutacion: Mutacion): Boolean {
        return this.tipoDeMutacion == mutacion.tipoDeMutacion
    }

    fun sonDeMismaEspecie(mutacion: Mutacion): Boolean {
        return this.especie.sonMismaEspecie(mutacion.especie)
    }

    constructor(tipoDeMutacion: TipoDeMutacion): this(){
        this.tipoDeMutacion = tipoDeMutacion
        if(tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
            potenciaDeMutacion = Random.decidir(100)
        } else {
            tipoDeVector = Random.decidirTipoVector()
        }
    }

}

enum class TipoDeMutacion {
    SupresionBiomecanica, BioalteracionGenetica
}





