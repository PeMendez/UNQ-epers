package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface DistritoDAO: MongoRepository<Distrito, String> {

    @Query("{ 'nombre': ?0 }")
    fun recuperarPorNombre(nombre: String): Optional<Distrito>


    //@Query("{ 'coordenadas' : { $geoIntersects : { $geometry : { type: 'GeoJsonPoint', coordinates: ?0 } } } }")
    //fun existeDistritoEnCoordenadas(coordenadas: List<GeoJsonPoint>): Boolean
}