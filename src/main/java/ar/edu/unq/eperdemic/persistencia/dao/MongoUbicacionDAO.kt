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


    @Query("{ 'coordenada' : { \$geoWithin : { \$centerSphere : [ [ ?0, ?1 ], 0.0156702 ] } }, 'coordenada.coordinates' : { \$ne : [ ?0, ?1 ] } }")
    fun findUbicacionesCercanas(longitude: Double, latitude: Double): List<UbicacionMongo>

    @Query("{ \$and: [ { 'coordenada' : { \$geoWithin : { \$centerSphere : [ [ ?0, ?1 ], 0.0156702 ] } } }, { 'coordenada.coordinates' : { \$ne : [ ?0, ?1 ] } }, { 'idRelacional' : ?2 } ] }")
    fun seEncuentraADistanciaAlcanzable(longitude: Double, latitude: Double, id: Long): Optional<UbicacionMongo>

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

