package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.Neo4jUbicacionDTO
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface Neo4jUbicacionDAO : Neo4jRepository<Neo4jUbicacionDTO, Long?> {


}