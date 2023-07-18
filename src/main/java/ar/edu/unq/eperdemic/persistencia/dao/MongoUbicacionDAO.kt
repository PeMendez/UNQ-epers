package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.bson.conversions.Bson
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface MongoUbicacionDAO: MongoRepository<UbicacionMongo, String> {

    @Query("{ 'coordenada': ?0 }")
    fun recuperarPorCoordenada(coordenada: GeoJsonPoint): Optional<UbicacionMongo>

    @Query("{'idRelacional' : ?0}")
    fun findByIdRelacional(idRelacional: Long): Optional<UbicacionMongo>

    @Query("{ \$and: [ { 'coordenada' : { \$geoWithin : { \$centerSphere : [ [ ?0, ?1 ], 0.0156702 ] } } }, { 'coordenada.coordinates' : { \$ne : [ ?0, ?1 ] } }, { 'idRelacional' : ?2 } ] }")
    fun seEncuentraADistanciaAlcanzable(longitude: Double, latitude: Double, id: Long): Optional<UbicacionMongo>

    @Aggregation(pipeline = [
        "{'\$match': {distrito: {\$ne: null}, idRelacional: {\$in: ?0}}}",
        "{'\$group': {_id: '\$distrito', totalEnfermas: {\$sum: 1}}}",
        "{'\$sort': {totalEnfermas: -1}}",
        "{'\$limit': 1}",
        "{'\$project': {_id: 0, nombre: '\$_id'}}",
    ])
    fun nombreDeDistritoMasEnfermo(idsDeUbicacionesMasEnfermas: List<Long>): Optional<String>
}

