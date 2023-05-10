package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.MutacionDTO
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/mutacion")
class MutacionControllerREST(private val mutacionService: MutacionService) {

    @PostMapping("/agregarMutacion/{especieId}")
    fun agregarMutacion(@PathVariable especieId: Long, @RequestBody mutacionDTO: MutacionDTO): MutacionDTO {
        val mutacion = mutacionService.agregarMutacion(especieId,mutacionDTO.aModelo())
        return MutacionDTO.desdeModelo(mutacion)
    }
}