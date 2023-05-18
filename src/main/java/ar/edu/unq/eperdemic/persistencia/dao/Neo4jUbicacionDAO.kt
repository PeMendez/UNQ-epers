package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.Neo4jUbicacionDTO
import ar.edu.unq.eperdemic.modelo.Ubicacion
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface Neo4jUbicacionDAO : Neo4jRepository<Neo4jUbicacionDTO, Long?> {

    @Query("MATCH (u: Neo4jUbicacionDTO {nombre: ${'$'}nombreDeUbicacion}) RETURN u")
    fun recuperarUbicacionPorNombre(nombreDeUbicacion: String): Neo4jUbicacionDTO?

    @Query("MATCH(n) DETACH DELETE n")
    fun detachDelete()
}