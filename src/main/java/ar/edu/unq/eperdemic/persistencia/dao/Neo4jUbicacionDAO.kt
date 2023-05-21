package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.Neo4jUbicacionDTO
import ar.edu.unq.eperdemic.modelo.Ubicacion
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface Neo4jUbicacionDAO : Neo4jRepository<Neo4jUbicacionDTO, Long?> {

    @Query("MATCH (u: Neo4jUbicacionDTO {nombre: ${'$'}nombreDeUbicacion}) RETURN u")
    fun recuperarUbicacionPorNombre(nombreDeUbicacion: String): Optional<Neo4jUbicacionDTO>

    @Query("MATCH(n) DETACH DELETE n")
    fun detachDelete()

    @Query("MATCH (u: Neo4jUbicacionDTO {idRelacional: ${'$'}idRelacional}) RETURN u")
    fun findByIdRelacional(idRelacional: Long): Optional<Neo4jUbicacionDTO>

    @Query("MATCH (origen: Neo4jUbicacionDTO {idRelacional: ${'$'}origenID}) " +
            "MATCH (destino: Neo4jUbicacionDTO {idRelacional: ${'$'}destinoID})" +
            "CREATE (origen)-[:CAMINO {type:${'$'}camino}]->(destino)")
    fun conectar(origenID:Long,destinoID:Long,camino:String)
}