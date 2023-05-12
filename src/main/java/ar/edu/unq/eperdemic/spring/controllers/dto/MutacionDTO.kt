package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.TipoDeMutacion
import ar.edu.unq.eperdemic.modelo.TipoDeVector

class MutacionDTO(
    val tipoDeMutacion: TipoDeMutacion,
    val especieId: Long,
    val tipoDeVector : TipoDeVector?,
    val poderDeMutacion: Int?) {

/*
    fun aModelo() : Mutacion {
        var mutacion = Mutacion()
        mutacion.tipoDeMutacion = this.tipoDeMutacion
        if (mutacion.tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
            mutacion.potenciaDeMutacion = this.poderDeMutacion
        } else {
            mutacion.tipoDeVector = this.tipoDeVector
        }
        return mutacion
    }

    companion object {
        fun desdeModelo(mutacion: Mutacion) =
            MutacionDTO(
                tipoDeMutacion = mutacion.tipoDeMutacion,
                especieId = mutacion.especie.id!!,
                tipoDeVector = mutacion.tipoDeVector,
                poderDeMutacion = mutacion.potenciaDeMutacion
            )
    }
*/
}

