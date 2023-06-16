package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface MongoUbicacionDAO: MongoRepository<UbicacionMongo, String> {

    @Query("{ 'coordenada': ?0 }")
    fun recuperarPorCoordenada(coordenada: GeoJsonPoint): Optional<UbicacionMongo>

    @Query("{'idRelacional' : ?0}")
    fun findByIdRelacional(idRelacional: Long): Optional<UbicacionMongo>

    /*@Query(value = "{ 'idRelacional': ?0, 'location': { \$geoNear: { near: { \$ref: 'ubicacionMongo', \$idRelacional: ?0 }, distanceField: 'distance', spherical: true }, \$maxDistance: 100 } }")
    fun distanciaAlcanzableEntreUbicaciones(ubicacionAMoverseId: Long, ubicacionActualId: Long): Boolean*/


    @Query("{ 'coordenada' : { \$geoWithin : { \$box : [ [ ?0, ?1 ], [ ?2, ?3 ] ] } }, \$and: [ { 'coordenada' : { \$ne : { 'type' : 'Point', 'coordinates' : [ ?4, ?5 ] } } } ] }")
    fun findUbicacionesCercanas(minLongitude: Double, minLatitude: Double, maxLongitude: Double, maxLatitude: Double, excludedLongitude: Double, excludedLatitude: Double): List<UbicacionMongo>

    @Query("{" +
            "  \"coordenada\": {" +
            "    \"\$geoWithin\": {" +
            "      \"\$center\": [[-10000,-10000], ?2]" +
            "    }" +
            "  }," +
            "  \"idRelacional\": ?0" +
            "}")
    fun distanciaAlcanzableEntreUbicacionesPablo4(ubicacionAMoverseId: Long, ubicacionActualId: Long, radius: Double): Optional<UbicacionMongo>
//[{"idRelacional": ?1},{"coordenada":1}]

    @Query("{" +
            "  \"coordenada\": {" +
            "    \"\$geoWithin\": {" +
            "      \"\$center\": [[?1,?2], ?3]" +
            "    }" +
            "  }," +
            "  \"idRelacional\": ?0" +
            "}")
    fun distanciaAlcanzableEntreUbicacionesPablo5(ubicacionAMoverseId: Long, latitud: Double, longitud: Double, radio: Double): Optional<UbicacionMongo>
}

