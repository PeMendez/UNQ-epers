package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
class UbicacionNeo4J() {
    @Id
    var id : Long? = null
    lateinit var nombre: String
    var idRelacional: Long? = null

    constructor(newName: String, idRelacional: Long) : this() {
        if (Check.validar(newName)) {
            this.nombre = newName
            this.idRelacional = idRelacional
        } else {
            throw NoPuedeEstarVacioOContenerCaracteresEspeciales("El nombre no puede ser vacío o contener caracteres especiales")
        }
    }

    //comento porque no anduvo////////////////////////////////////////////////////
    //@Relationship(type = "CAMINO", direction = Relationship.Direction.OUTGOING)
    //var ubicaciones: MutableSet<UbicacionNeo4J> = mutableSetOf()
}