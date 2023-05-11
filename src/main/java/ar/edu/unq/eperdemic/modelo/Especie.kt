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
    @OneToMany(mappedBy = "especie", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    lateinit var mutaciones: MutableSet<Mutacion>


    constructor(pathogen: Patogeno, name: String, pais: String, mutaciones: MutableSet<Mutacion> = HashSet()): this() {
        if (Check.validar(name) && Check.validar(pais)){
            this.patogeno = pathogen
            this.nombre = name
            this.paisDeOrigen = pais
            this.mutaciones = mutaciones
        } else {
            throw NoPuedeEstarVacioOContenerCaracteresEspeciales("El nombre o el pais no puede ser vacíos o contener caracteres especiales")
        }
    }

    fun agregarMutacion(mutacion: Mutacion) : Mutacion {
        //mutacion.addEspecie(this)
        mutaciones.add(mutacion)
        return mutacion
    }


    fun capacidadDeBiomecanizacion(): Int {
        return this.patogeno.capacidadDeBiomecanizacion()
    }

    fun capacidadDeDefensa(): Int {
        return this.patogeno.capacidadDeDefensa()
    }

    fun capacidadDeContagio(): Int {
        return this.patogeno.capacidadDeContagio()
    }

}




