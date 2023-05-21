package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.Neo4jUbicacionDTO
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
class UbicacionNeo4J {
    @Id
    var id : Long? = null
    lateinit var nombre: String

    @Relationship
    var ubicaciones: MutableSet<Neo4jUbicacionDTO> = mutableSetOf()

    fun aModelo(): Ubicacion{
        val ubicacion = Ubicacion()
        ubicacion.nombre = this.nombre
        ubicacion.id = this.id
        return ubicacion
    }

}