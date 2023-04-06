package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Patogeno(var tipo: String) : Serializable{

    @Id
    var id : Long? = null
    var cantidadDeEspecies: Int = 0

    override fun toString(): String {
        return tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie {
        cantidadDeEspecies++
        return Especie(this, nombreEspecie, paisDeOrigen)
    }
}