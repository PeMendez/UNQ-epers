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

    @Query(
        "{\$let: { " +
                "vars: { " +
                "ubicacionAMoverse: { \$first: { \$filter: { input: \"ubicacionAMoverseId\", as: \"point\", cond: { \$eq: [\"\$\$point.idRelacional\", ?0] } } } }, " +
                "ubicacionActual: { \$first: { \$filter: { input: \"ubicacionActualId\", as: \"point\", cond: { \$eq: [\"\$\$point.idRelacional\", ?1] } } } } " +
                "}, " +
                "in: {" +
                "\$and: [ " +
                "{ \"_id\": ?0 }, " +
                "{ \"coordenada\": { \$geoWithin: { \$center: [ [\"\$\$ubicacionActual.coordenada.longitude\", \"\$\$ubicacionActual.coordenada.latitude\"], \$2 ] } } } " +
                "]} } }")
    fun distanciaAlcanzableEntreUbicacionesPablo1(ubicacionAMoverseId: Long, ubicacionActualId: Long, radius: Int): Boolean

    @Query(
        "{\$let: { " +
                "  vars: { " +
                "    ubicacionAMoverse: {'idRelacional' : ?0}, " +
                "    ubicacionActual: {'idRelacional' : ?1} " +
                "  }, " +
                "  in: { " +
                "    \$and: [ " +
                "      { \"ubicacionAMoverse.coordenada\": { \$geoWithin: { \$center: [ [\"\$\$ubicacionActual.coordenada.longitude\", \"\$\$ubicacionActual.coordenada.latitude\"], \$2 ] } } } " +
                "    ] " +
                "  } " +
                "} }"
    )
    fun distanciaAlcanzableEntreUbicacionesPablo2(ubicacionAMoverseId: Long, ubicacionActualId: Long, radius: Int): Boolean

    @Query(
        "{ " +
                "  \"ubicacionAMoverse.idRelacional\": ?0, " +
                "  \"ubicacionAMoverse.coordenada\": { " +
                "    \$geoWithin: { " +
                "      \$center: [ " +
                "        [ \"\$\$ubicacionActual.coordenada.longitude\", \"\$\$ubicacionActual.coordenada.latitude\" ], " +
                "        ?2 " +
                "      ] " +
                "    } " +
                "  } " +
                "}"
    )
    fun distanciaAlcanzableEntreUbicacionesPablo3(ubicacionAMoverseId: Long, ubicacionActualId: Long, radius: Int): Boolean


    @Query(
        "{ " +
                "  \"ubicacionAMoverse.idRelacional\": ?0, " +
                "  \"ubicacionAMoverse.coordenada\": { " +
                "    \$geoWithin: { " +
                "      \$center: [ " +
                "        \$\$\$ubicacionActual.coordenada.longitude, " +
                "        \$\$\$ubicacionActual.coordenada.latitude, " +
                "        ?2 " +
                "      ] " +
                "    } " +
                "  } " +
                "}"
    )
    fun distanciaAlcanzableEntreUbicacionesPablo4(ubicacionAMoverseId: Long, ubicacionActualId: Long, radius: Int): Boolean

}

