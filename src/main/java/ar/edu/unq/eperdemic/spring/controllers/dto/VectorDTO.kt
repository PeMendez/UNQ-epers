package ar.edu.unq.eperdemic.spring.controllers.dto


import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

class VectorDTO(
    val vectorID : Long,
    val tipoDeVector : TipoDeVector,
    val ubicacion: Ubicacion) {

    //CONSULTAR. agregamos id por si ya estaba en la base
    fun aModelo() : Vector {
        val vector = Vector(this.tipoDeVector, this.ubicacion)
        vector.id = vectorID
        return vector
    }
    companion object {
        fun desdeModelo(vector: Vector) =
            VectorDTO(
                vectorID = vector.id!!,
                tipoDeVector = vector.tipo,
                ubicacion = vector.ubicacion
            )
   }
}