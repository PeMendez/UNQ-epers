package ar.edu.unq.eperdemic.modelo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document

@Document("Ubicacion")
class UbicacionMongo {
    @Id
    var id: String? = null
    var idRelacional: Long? = null
    lateinit var nombre: String
    lateinit var coordenada: GeoJsonPoint
    var distrito: String? = null


    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {
    }
    constructor(idRelacional: Long, nombre: String, coordenada: GeoJsonPoint, distrito: String) {
        this.idRelacional = idRelacional
        this.nombre = nombre
        this.coordenada = coordenada
        this.distrito = distrito
    }
    constructor(idRelacional: Long, nombre: String, coordenada: GeoJsonPoint) {
        this.idRelacional = idRelacional
        this.nombre = nombre
        this.coordenada = coordenada
    }

    fun aUbicacion(): Ubicacion {
        val ubicacion = Ubicacion(this.nombre)
        ubicacion.id = this.idRelacional
        return ubicacion
    }

}