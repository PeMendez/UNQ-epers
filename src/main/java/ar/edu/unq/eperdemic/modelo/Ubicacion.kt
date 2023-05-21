package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import javax.persistence.*
@Entity
class Ubicacion() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null
    @Column(unique=true)
    lateinit var nombre: String

    constructor(newName: String): this() {
        if (Check.validar(newName)){
            this.nombre = newName
        } else {
            throw NoPuedeEstarVacioOContenerCaracteresEspeciales("El nombre no puede ser vacío o contener caracteres especiales")
        }
    }
    fun aUbicacionNeo4J():UbicacionNeo4J{
        val nuevaUbicacionNeo4J = UbicacionNeo4J()

        nuevaUbicacionNeo4J.nombre = this.nombre
        nuevaUbicacionNeo4J.idRelacional = this.id

        return nuevaUbicacionNeo4J
    }
}