package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.UbicacionNeo4J
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface Neo4jUbicacionDAO : Neo4jRepository<UbicacionNeo4J, Long?> {

    @Query("MATCH (u: UbicacionNeo4J {nombre: ${'$'}nombreDeUbicacion}) RETURN u")
    fun recuperarUbicacionPorNombre(nombreDeUbicacion: String): Optional<UbicacionNeo4J>

    @Query("MATCH(n) DETACH DELETE n")
    fun detachDelete()

    @Query("MATCH (u: UbicacionNeo4J {idRelacional: ${'$'}idRelacional}) RETURN u")
    fun findByIdRelacional(idRelacional: Long): Optional<UbicacionNeo4J>

    @Query("MATCH (origen: UbicacionNeo4J {idRelacional: ${'$'}origenID}) " +
            "MATCH (destino: UbicacionNeo4J {idRelacional: ${'$'}destinoID})" +
            "CREATE (origen)-[:CAMINO {tipoDeCamino:${'$'}tipoDeCamino}]->(destino)")
    fun conectar(origenID:Long,destinoID:Long,tipoDeCamino:String)

    @Query("MATCH (origen: UbicacionNeo4J {idRelacional: ${'$'}origenID}) " +
            "MATCH (destino: UbicacionNeo4J {idRelacional: ${'$'}destinoID})" +
            "RETURN EXISTS ((origen)-[:CAMINO]->(destino))")
    fun hayConexionDirecta(origenID:Long,destinoID:Long): Boolean
}