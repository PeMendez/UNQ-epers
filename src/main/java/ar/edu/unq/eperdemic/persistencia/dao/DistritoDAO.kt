package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface DistritoDAO: MongoRepository<Distrito, String> {

    @Query("{ 'nombre': ?0 }")
    fun recuperarPorNombre(nombre: String): Optional<Distrito>


    @Query("{'coordenadas' : { \$in: [?0] }}", exists = true)
    fun existeDistritoEnCoordenadas(coordenadas: GeoJsonPolygon): Boolean

    @Query("{ 'coordenadas' : { \$geoIntersects: { \$geometry: { type: 'Point', 'coordinates': ?0 }}}}")
    fun distritoEnCoordenada(poligono: List<Double>): Optional<Distrito>

    @Query("[{'\$match': {'distrito': { '\$ne': null },'idRelacional': { '\$in': ?0 } } }, {'\$group': {'distrito': '\$distrito','totalEnfermas': { '\$sum': 1 } } }, {'\$sort': { 'totalEnfermas': -1 } }, {'\$limit': 1 } ]")
    fun distritoMasEnfermo(idsDeUbicacionesMasEnfermas: List<Long>): Optional<Distrito>


}
