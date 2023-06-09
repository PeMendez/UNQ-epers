package ar.edu.unq.eperdemic.modelo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.awt.Point


@Document("Ubicacion")
class UbicacionMongo {
    @Id
    var id: String? = null
    var idRelacional: Long? = null
    lateinit var nombre: String
    lateinit var coordenada: GeoJsonPoint


    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {
    }
    constructor(idRelacional: Long, nombre: String, coordenada: GeoJsonPoint) {
        this.idRelacional = idRelacional
        this.nombre = nombre
        this.coordenada = coordenada
    }

}