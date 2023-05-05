package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.TipoDeMutacion
import ar.edu.unq.eperdemic.modelo.TipoDeVector

class MutacionDTO(
    val tipoDeMutacion: TipoDeMutacion,
    val especieId: Long,
    val tipoDeVector : TipoDeVector?,
    val poderDeMutacion: Int?
) {

    fun aModelo() : Mutacion {
        var mutacion = Mutacion()
        mutacion.tipoDeMutacion = this.tipoDeMutacion
        mutacion.especieId = this.especieId
        mutacion.tipoDeVector = this.tipoDeVector!!
        mutacion.poderDeMutacion = this.poderDeMutacion!!
        return mutacion
    }

    companion object {
        fun desdeModelo(mutacion: Mutacion) =
            MutacionDTO(
                tipoDeMutacion = mutacion.tipoDeMutacion,
                especieId = mutacion.especieId!!,
                tipoDeVector = mutacion.tipoDeVector,
                poderDeMutacion = mutacion.poderDeMutacion

            )
    }

}