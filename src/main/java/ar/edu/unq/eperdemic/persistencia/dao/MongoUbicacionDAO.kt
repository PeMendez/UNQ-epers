package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface MongoUbicacionDAO: MongoRepository<UbicacionMongo, String> {

    @Query("{ 'coordenada': { '\$eq': ?0 } }")
    fun recuperarPorCoordenada(coordenadaABuscar: GeoJsonPoint): Optional<UbicacionMongo>

    @Query("{ 'coordenada': ?0 }")
    fun existeUbicacionPorCoordenada(coordenada: GeoJsonPoint): Optional<UbicacionMongo>
}

