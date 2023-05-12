package ar.edu.unq.eperdemic.spring.controllers.dto

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno

class EspecieDTO(
    val nombre: String,
    val paisDeOrigen: String,
    val patogenoId: Long?
) {
    companion object {
        fun desdeModelo(especie: Especie) =
            EspecieDTO (
                nombre = especie.nombre,
                paisDeOrigen = especie.paisDeOrigen,
                patogenoId = if (especie.patogeno.id != null) especie.patogeno.id else null
            )
    }


    fun aModelo(patogeno: Patogeno): Especie {

        return Especie(patogeno, this.nombre, this.paisDeOrigen)
    }

}
