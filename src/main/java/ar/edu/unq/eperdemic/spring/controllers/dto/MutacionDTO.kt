package ar.edu.unq.eperdemic.spring.controllers.dto

class MutacionDTO(
    val tipoDeMutacion: TipoDeMutacion,
    val especieId: Long,
    val tipoDeVector : TipoDeVector?,
    val poderDeMutacion: Int?) {

    enum class TipoDeVector {
        Persona, Insecto, Animal
    }
    enum class TipoDeMutacion {
        Supresion_Biomecanica, Bioalteracion_Genetica,
    }

//    TODO: implementar aModelo
//    fun aModelo() : Mutacion {
//        return null
//    }

//    TODO: implementar desdeModelo
//    companion object {
//        fun desdeModelo(Mutacion:mutacion) = null
//    }

}