package ar.edu.unq.eperdemic.modelo

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class Distrito {

    private lateinit var coordenadas: List<GeoJsonPoint>
    protected constructor() {}
    constructor(coordenadas: List<GeoJsonPoint>) {
        this.coordenadas = coordenadas
    }
}