package ar.edu.unq.eperdemic.modelo

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
class UbicacionNeo4J {
    @Id
    var id : Long? = null
    lateinit var nombre: String
    var idRelacional: Long? = null
    @Relationship
    var ubicaciones: MutableSet<UbicacionNeo4J> = mutableSetOf()
}