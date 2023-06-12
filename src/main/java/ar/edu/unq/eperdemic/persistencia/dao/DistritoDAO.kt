package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.data.mongodb.repository.MongoRepository

interface DistritoDAO: MongoRepository<Distrito, String> {

}