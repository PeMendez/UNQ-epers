package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.PatogenoDTO
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/patogeno")
class PatogenoControllerREST(private val patogenoService: PatogenoService) {

  @PostMapping
  fun create(@RequestBody patogeno: Patogeno): PatogenoDTO {
    val patogeno = patogenoService.crearPatogeno(patogeno)
    return PatogenoDTO.desdeModelo(patogeno)
  }

  @PostMapping("/addEspecie/{ubicacionId}")
  fun agregarEspecie(@PathVariable ubicacionId: Long, @RequestBody especieDTO: EspecieDTO): EspecieDTO {
    val especie = patogenoService.agregarEspecie(especieDTO.patogenoId!!, especieDTO.nombre, ubicacionId)
    return EspecieDTO.desdeModelo(especie)
  }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long) = PatogenoDTO.desdeModelo(patogenoService.recuperarPatogeno(id))

  @GetMapping("/patogenos")
  fun getAll() = patogenoService.recuperarATodosLosPatogenos().map{ PatogenoDTO.desdeModelo(it)}

  @GetMapping("/especies/{id}")
  fun especiesDePatogeno(@PathVariable id: Long) = patogenoService.especiesDePatogeno(id).map { EspecieDTO.desdeModelo(it) }

  @GetMapping("/esPandemia/{especieId}")
  fun esPandemia(@PathVariable especieId: Long) = patogenoService.esPandemia(especieId)

}