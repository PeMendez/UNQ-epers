package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Patogeno

class PatogenoDTO (
    val id:Long?,
    val tipo : String,
    val cantidadDeEspecies:Int?,
){
    companion object{
        fun desdeModelo(patogeno: Patogeno) =
            PatogenoDTO(
                id = patogeno.id,
                tipo = patogeno.tipo,
                cantidadDeEspecies = patogeno.cantidadDeEspecies
            )
    }
    fun aModelo(): Patogeno {
        val patogeno = Patogeno()
        patogeno.id = this.id
        patogeno.tipo = this.tipo
        patogeno.cantidadDeEspecies = this.cantidadDeEspecies!!
        return patogeno
    }
}
