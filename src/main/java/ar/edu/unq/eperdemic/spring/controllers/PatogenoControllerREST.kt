package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.exceptions.NingunVectorAInfectarEnLaUbicacionDada
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.PatogenoDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/patogeno")
class PatogenoControllerREST(private val patogenoService: PatogenoService) {

  @Autowired
  lateinit var ubicacionDAO: UbicacionDAO

  @PostMapping
  fun create(@RequestBody patogeno: PatogenoDTO): PatogenoDTO {
    val patogenoCreado = patogenoService.crearPatogeno(patogeno.aModelo())
    return PatogenoDTO.desdeModelo(patogenoCreado)
  }

  @PostMapping("/addEspecie/{id}")
  fun agregarEspecie(@PathVariable id: Long, @RequestBody especieDTO: EspecieDTO): EspecieDTO {
    val ubicacion = ubicacionDAO.recuperarUbicacionPorNombre(especieDTO.paisDeOrigen)
    val especie = patogenoService.agregarEspecie(id, especieDTO.nombre, ubicacion.id!!)
    return EspecieDTO.desdeModelo(especie)
  }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long) = PatogenoDTO.desdeModelo(patogenoService.recuperarPatogeno(id))

  @GetMapping("/patogenos")
  fun getAll(@RequestParam("offset", defaultValue = "0") offset: Int, @RequestParam("limit", defaultValue = "10") limit: Int): List<PatogenoDTO> {
    val pageable: Pageable = PageRequest.of(offset, limit)
    return patogenoService.recuperarATodosLosPatogenos(pageable).map { PatogenoDTO.desdeModelo(it)}.toList()
  }

  @GetMapping("/especies/{id}")
  fun especiesDePatogeno(@PathVariable id: Long) = patogenoService.especiesDePatogeno(id).map { EspecieDTO.desdeModelo(it) }

  @GetMapping("/esPandemia/{especieId}")
  fun esPandemia(@PathVariable especieId: Long) = patogenoService.esPandemia(especieId)

  @ExceptionHandler(NoExisteElid::class)
  fun handleNotFoundException(ex: NoExisteElid): ResponseEntity<String> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
  }

  @ExceptionHandler(NingunVectorAInfectarEnLaUbicacionDada::class)
  fun handleNotFoundException(ex: NingunVectorAInfectarEnLaUbicacionDada): ResponseEntity<String> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
  }

}