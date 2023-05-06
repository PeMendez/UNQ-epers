package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

class VectorDTO(
    val tipoDeVector : TipoDeVector,
    val ubicacion: Ubicacion) {


    fun aModelo() : Vector {
        return Vector(this.tipoDeVector, this.ubicacion)
    }


    companion object {
        fun desdeModelo(vector: Vector) =
            VectorDTO(
                tipoDeVector = vector.tipo,
                ubicacion = vector.ubicacion
            )
   }



}