package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Ubicacion

class VectorDTO(val tipoDeVector : TipoDeVector,
                        val ubicacion: Ubicacion) {

    enum class TipoDeVector {
        Persona, Insecto, Animal
    }

//    TODO: implementar aModelo
//    fun aModelo() : Vector {
//        return null
//    }

//    TODO: implementar desdeModelo
//    companion object {
//        fun desdeModelo(Vector:vector) = null
//    }

}