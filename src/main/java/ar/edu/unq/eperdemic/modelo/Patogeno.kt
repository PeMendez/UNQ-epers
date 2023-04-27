package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*

@Entity
class Patogeno(var tipo: String) : Serializable{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    var id : Long? = null
    var cantidadDeEspecies: Int = 0
    var capacidadDeContagio: Int= Random.decidir(100)
    var capacidadDeDefensa: Int= Random.decidir(100)
    var capacidadDeBiomecanizacion: Int = Random.decidir(100)

    @OneToMany(mappedBy = "patogeno", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especies: MutableSet<Especie> = HashSet()

    override fun toString(): String {
        return tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie {
        cantidadDeEspecies++
        val especie = Especie(this, nombreEspecie, paisDeOrigen)
        especies.add(especie)
        return especie
    }
}