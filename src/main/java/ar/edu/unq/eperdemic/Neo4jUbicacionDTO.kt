package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Check
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
class Neo4jUbicacionDTO() {

    @Id
    @GeneratedValue
    var id: Long? = null
    lateinit var nombre: String
    var idRelacional: Long? = null

    @Relationship
    var ubicaciones: MutableSet<Neo4jUbicacionDTO> = mutableSetOf()

    constructor(newName: String) : this() {
        if (Check.validar(newName)) {
            this.nombre = newName
        } else {
            throw NoPuedeEstarVacioOContenerCaracteresEspeciales("El nombre no puede ser vacío o contener caracteres especiales")
        }
    }

    fun aModelo(): Ubicacion {
        return Ubicacion(nombre)
    }

    companion object {
        fun desdeModelo(ubicacion: Ubicacion): Neo4jUbicacionDTO {
            val ubi = Neo4jUbicacionDTO(ubicacion.nombre)
            ubi.idRelacional = ubicacion.id!!
            return ubi
        }
    }

}