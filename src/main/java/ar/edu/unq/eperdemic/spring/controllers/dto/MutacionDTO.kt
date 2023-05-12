package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.TipoDeMutacion
import ar.edu.unq.eperdemic.modelo.TipoDeVector

class MutacionDTO(
    val tipoDeMutacion: TipoDeMutacion,
    val especieId: Long,
    val tipoDeVector : TipoDeVector?,
    val poderDeMutacion: Int?
) {

/*
    fun aModelo() : Mutacion {
        if(tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica ) {
            var mutacion = TipoDeMutacion.SupresionBiomecanica
            mutacion.
        }
        return mutacion
    }

    companion object {
        fun desdeModelo(mutacion: Mutacion) =
            MutacionDTO(
                tipoDeMutacion = mutacion.tipoDeMutacion,
                especieId = mutacion.especie.id!!,
                tipoDeVector = mutacion.tipoDeVector,
                poderDeMutacion = mutacion.poderDeMutacion

            )
    }
*/
}

