package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*
import kotlin.random.Random

@Entity
class Patogeno(var tipo: String) : Serializable{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    var id : Long? = null
    var cantidadDeEspecies: Int = 0
    var capacidadDeContagio: Int= (1..100).random()
    var capacidadDeDefensa: Int= (1..100).random()


    @OneToMany(mappedBy = "patogeno", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especie:  MutableSet<Especie> = HashSet()

    override fun toString(): String {
        return tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie {
        cantidadDeEspecies++
        return Especie(this, nombreEspecie, paisDeOrigen)
    }
}