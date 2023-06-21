package ar.edu.unq.eperdemic.modelo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.core.mapping.Document

@Document("Distrito")
class Distrito {

    @Id
    var id: String? = null
    lateinit var coordenadas: GeoJsonPolygon
    lateinit var nombre: String
    protected constructor() {}
    constructor(nombre: String, coordenadas: GeoJsonPolygon) {
        this.nombre = nombre
        this.coordenadas = coordenadas
    }
}