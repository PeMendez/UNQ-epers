package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Ubicacion

class UbicacionDTO(
    var nombreDeLaUbicacion: String,
    val ubicacionId: Long?) {


    fun aModelo() : Ubicacion {
        return Ubicacion(this.nombreDeLaUbicacion)
    }

    companion object {
        fun desdeModelo(ubicacion:Ubicacion) =
            UbicacionDTO(
                nombreDeLaUbicacion = ubicacion.nombre,
                ubicacionId = ubicacion.id!!
            )
    }
}