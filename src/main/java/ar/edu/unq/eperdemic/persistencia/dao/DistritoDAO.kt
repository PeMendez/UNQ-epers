package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface DistritoDAO: MongoRepository<Distrito, String> {

    @Query("{ 'nombre': ?0 }")
    fun recuperarPorNombre(nombre: String): Optional<Distrito>


    @Query("{'coordenadas' : { \$in: ?0 }}", exists = true)
    fun existeDistritoEnCoordenadas(coordenadas: GeoJsonPolygon): Boolean

    @Query("{ 'distrito.geometry' : { \$geoIntersects: { \$geometry: {type: 'Point', 'coordenada.coordinates'  : ?0 }}}}")
    fun distritoEnCoordenada(x: Double, y: Double): Optional<Distrito>


    @Query("{ 'distrito.geometry' : { \$geoIntersects: { \$geometry: { type: 'Polygon', 'coordinates': [ [ ?0 ] ] }}}}")
    fun distritoEnPoligono(poligono: GeoJsonPoint): Optional<Distrito>

}
