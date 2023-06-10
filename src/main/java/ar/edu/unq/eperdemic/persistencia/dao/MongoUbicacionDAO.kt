package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface MongoUbicacionDAO: MongoRepository<UbicacionMongo, String> {

    @Query("{ 'coordenada': ?0 }")
    fun recuperarPorCoordenada(coordenada: GeoJsonPoint): Optional<UbicacionMongo>

    @Query
    fun distanciaEntreUbicaciones(ubicacionIdDestino: Long, ubicacionIdOrigen: Long): Int
}

