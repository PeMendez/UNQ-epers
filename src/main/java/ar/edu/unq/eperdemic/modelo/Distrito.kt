package ar.edu.unq.eperdemic.modelo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document

@Document("Distrito")
class Distrito {

    @Id
    var id: String? = null
    lateinit var coordenadas: List<GeoJsonPoint>
    lateinit var nombre: String
    protected constructor() {}
    constructor(nombre: String, coordenadas: List<GeoJsonPoint>) {
        this.nombre = nombre
        this.coordenadas = coordenadas
    }
}