package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class DistritoDTO(
    val nombre: String,
    val coordenadas: List<GeoJsonPoint>
) {
    companion object {
        fun desdeModelo(distrito: Distrito) =
            DistritoDTO(
                nombre = distrito.nombre,
                coordenadas = distrito.coordenadas
            )
    }


    fun aModelo(): Distrito {

        return Distrito(this.nombre, this.coordenadas)
    }
}