package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.PatogenoDTO
import org.springframework.web.bind.annotation.*

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/patogeno")
class PatogenoControllerREST(private val patogenoService: PatogenoService) {

  @PostMapping
  fun create(@RequestBody patogeno: Patogeno): PatogenoDTO {
    val patogeno = patogenoService.crearPatogeno(patogeno)
    return PatogenoDTO.from(patogeno)
  }

  @PostMapping("/{id}")
  fun agregarEspecie(@PathVariable id: Long, @RequestBody especieDTO: EspecieDTO): EspecieDTO {
    val especie = patogenoService.agregarEspecie(id, especieDTO.nombre, especieDTO.paisDeOrigen)
    return EspecieDTO.from(especie)
  }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long) = PatogenoDTO.from(patogenoService.recuperarPatogeno(id))

  @GetMapping
  fun getAll() = patogenoService.recuperarATodosLosPatogenos().map{ PatogenoDTO.from(it)}


}