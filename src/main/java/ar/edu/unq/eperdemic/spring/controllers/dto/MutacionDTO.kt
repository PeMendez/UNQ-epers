package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.TipoDeMutacion
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.CombinacionDeDatosIncorrecta

class MutacionDTO(
    val tipoDeMutacion: TipoDeMutacion,
    val especieId: Long,
    val tipoDeVector : TipoDeVector?,
    val poderDeMutacion: Int?) {


    fun aModelo() : Mutacion {
        val mutacion = Mutacion(this.tipoDeMutacion)
        if (tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica && poderDeMutacion != null){
            mutacion.potenciaDeMutacion = poderDeMutacion
        }else if (tipoDeMutacion == TipoDeMutacion.BioalteracionGenetica && tipoDeVector != null){
            mutacion.tipoDeVector = tipoDeVector
        }
        if (tipoDeMutacion ==TipoDeMutacion.SupresionBiomecanica && tipoDeVector != null){
            throw CombinacionDeDatosIncorrecta("el tipo supresion biomecanica no tiene un tipo de vector")
        }
        if (tipoDeMutacion == TipoDeMutacion.BioalteracionGenetica && poderDeMutacion != null){
            throw CombinacionDeDatosIncorrecta("el tipo bioalteracion genetica no tiene un poder de mutación")
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

}

