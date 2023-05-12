package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.MutacionDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/mutacion")
class MutacionControllerREST(private val mutacionService: MutacionService) {

    @PostMapping("/agregarMutacion")
    fun agregarMutacion(@RequestBody mutacionDTO: MutacionDTO): MutacionDTO {
        val mutacion = mutacionService.agregarMutacion(mutacionDTO.especieId,mutacionDTO.aModelo())
        return MutacionDTO.desdeModelo(mutacion)
    }

    @ExceptionHandler(NoExisteElid::class)
    fun handleNotFoundException(ex: NoExisteElid): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

}