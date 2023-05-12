package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.ConvinacionDeDatosIncorrecta
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.MutacionDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = MutacionDTO.desdeModelo(mutacionService.recuperarMutacion(id))

    @GetMapping("/mutaciones")
    fun getAll(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") size:Int): List<MutacionDTO> {
        val pageable: Pageable = PageRequest.of(page, size)
        val mutacionesPage: Page<Mutacion> = mutacionService.recuperarTodas(pageable)
        return mutacionesPage.map { MutacionDTO.desdeModelo(it) }.toList()
    }

    @ExceptionHandler(NoExisteElid::class)
    fun handleNotFoundException(ex: NoExisteElid): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(ConvinacionDeDatosIncorrecta::class)
    fun handleNotFoundException(ex: ConvinacionDeDatosIncorrecta): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(InvalidFormatException::class)
    fun handleInvalidFormatException(e: InvalidFormatException): ResponseEntity<String> {
        val expectedValues = e.targetType.enumConstants.joinToString(", ")
        val errorMessage = "Valor inválido para el campo 'tipo'. Se esperaba uno de los siguientes valores: $expectedValues."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }
}