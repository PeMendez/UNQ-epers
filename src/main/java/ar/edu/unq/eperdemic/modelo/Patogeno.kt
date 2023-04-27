package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import java.io.Serializable
import javax.persistence.*

@Entity
class Patogeno() : Serializable{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    var id : Long? = null
    lateinit var tipo : String
    var cantidadDeEspecies: Int = 0
    var capacidadDeContagio: Int= Random.decidir(100)
    var capacidadDeDefensa: Int= Random.decidir(100)
    var capacidadDeBiomecanizacion: Int = Random.decidir(100)

    @OneToMany(mappedBy = "patogeno", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especies: MutableSet<Especie> = HashSet()

    constructor(newTipo: String): this() {
        if (Check.validar(newTipo)){
            this.tipo = newTipo
        }
        else {
            throw NoPuedeEstarVacioOContenerCaracteresEspeciales("El tipo no puede ser vacío o contener caracteres especiales")

        }
    }
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