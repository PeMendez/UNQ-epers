package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import javax.persistence.*

@Entity
class Especie() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
    @ManyToOne
    lateinit var patogeno: Patogeno
    lateinit var nombre: String
    lateinit var paisDeOrigen: String

    constructor(pathogen: Patogeno, name: String, pais: String ): this() {
        if (Check.validar(name) && Check.validar(pais)){
            this.patogeno = pathogen
            this.nombre = name
            this.paisDeOrigen = pais
        }
        else {
            throw NoPuedeEstarVacioOContenerCaracteresEspeciales("El nombre o el pais no puede ser vacíos o contener caracteres especiales")

        }
    }

}




