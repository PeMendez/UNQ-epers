package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.UbicacionNeo4J
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
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

    @Query("MATCH (origen: UbicacionNeo4J {nombre: ${'$'}origenNombre})" +
            "MATCH (origen)-[:CAMINO*1]-(conectado: UbicacionNeo4J)" +
            "RETURN conectado")
    fun conectados(origenNombre:String): List<UbicacionNeo4J>

    @Query("MATCH (u1:UbicacionNeo4J {nombre: ${'$'}ubicacion1Nombre})-[c:CAMINO]->(u2:UbicacionNeo4J {nombre: ${'$'}ubicacion2Nombre}) " +
            "RETURN c.tipoDeCamino")
    fun conectadosPorCamino(ubicacion1Nombre: String, ubicacion2Nombre: String): Optional<String>

    @Query("MATCH (origen:UbicacionNeo4J {nombre: ${'$'}origenNombre})" +
            "MATCH (destino:UbicacionNeo4J {nombre: ${'$'}destinoNombre})" +
            "MATCH caminosMasCortos = allShortestPaths((origen)-[c:CAMINO]-(destino))" +
            "WHERE ALL(rel in relationships(caminosMasCortos) WHERE rel.tipoDeCamino IN (UNWIND \$caminosCompatibles) " +
            "RETURN caminosMasCortos")
    fun caminosCompatibles(caminosCompatibles:List<String>,origenNombre:String,destinoNombre:String): Optional<List<List<UbicacionNeo4J>>>

    @Query("MATCH (origen:UbicacionNeo4J {nombre: ${'$'}origenNombre})" +
            "MATCH (destino:UbicacionNeo4J {nombre: ${'$'}destinoNombre})" +
            "MATCH caminosMasCortos = allShortestPaths((origen)-[CAMINO*]-(destino))" +
            "WHERE ALL(rel in relationships(caminosMasCortos) WHERE rel.tipoDeCamino IN \$caminosCompatibles) " +
            "RETURN caminosMasCortos")
    fun caminoMasCortoParaEntre(caminosCompatibles:List<String>,origenNombre:String,destinoNombre:String): Optional<List<UbicacionNeo4J>>

    @Query("MATCH (origen:UbicacionNeo4J {nombreUbicacion: ${'$'}ubicacionOrigen})," +
           "MATCH (destino:UbicacionNeo4J {nombreUbicacion: ${'$'}ubicacionDestino}), " +
           //"MATCH (origen)-[r]->(destino) WHERE type(r) IN \$caminosCompatibles RETURN r")
            "path = shortestpath((origen)-[:${'$'}caminosCompatibles}*]->(destino)) " +
            "RETURN [x in nodes(path) | x];")
    fun caminoMasCortoParaEntre(ubicacionOrigen: String,
                                ubicacionDestino: String,
                                caminosCompatibles: String) : List<String>

}