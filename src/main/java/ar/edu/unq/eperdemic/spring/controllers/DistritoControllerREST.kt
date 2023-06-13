package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.exceptions.CoordenadasParaUnDistritoRepetidas
import ar.edu.unq.eperdemic.modelo.exceptions.NoHayDistritoInfectado
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeDistritoRepetido
import ar.edu.unq.eperdemic.services.DistritoService
import ar.edu.unq.eperdemic.spring.controllers.dto.DistritoDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/distrito")
class DistritoControllerREST(private val distritoService: DistritoService) {

  @PostMapping
  fun create(@RequestBody distrito: DistritoDTO): DistritoDTO {
      val distritoCreado = distritoService.crear(distrito.aModelo())
      return DistritoDTO.desdeModelo(distritoCreado)
  }

  @GetMapping("/distritoMasEnfermo")
  fun distritoMasEnfermo(): DistritoDTO {
      return DistritoDTO.desdeModelo(distritoService.distritoMasEnfermo())
  }

  @ExceptionHandler(NoHayDistritoInfectado::class)
  fun handleNotFoundException(ex: NoHayDistritoInfectado): ResponseEntity<String> {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
  }

  @ExceptionHandler(NombreDeDistritoRepetido::class)
  fun handleNotFoundException(ex: NombreDeDistritoRepetido): ResponseEntity<String> {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
  }

  @ExceptionHandler(CoordenadasParaUnDistritoRepetidas::class)
  fun handleNotFoundException(ex: CoordenadasParaUnDistritoRepetidas): ResponseEntity<String> {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
  }

}