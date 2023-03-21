package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Patogeno

class PatogenoDTO (val id:Long?, val tipo : String, val cantidadDeEspecies:Int?){

    companion object {
        fun from(patogeno: Patogeno) =
            PatogenoDTO(patogeno.id, patogeno.tipo, patogeno.cantidadDeEspecies)
    }
}