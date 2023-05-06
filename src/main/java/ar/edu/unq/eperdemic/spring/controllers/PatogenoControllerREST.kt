package ar.edu.unq.eperdemic.spring.controllers

/*
@RestController
@CrossOrigin
//@ServiceREST
@RequestMapping("/patogeno")
class PatogenoControllerREST(private val patogenoService: PatogenoService) {

  @PostMapping
  fun create(@RequestBody patogeno: Patogeno): PatogenoDTO {
    val patogeno = patogenoService.crearPatogeno(patogeno)
    return PatogenoDTO.desdeModelo(patogeno)
  }

  @PostMapping("/{id}")
  fun agregarEspecie(@PathVariable id: Long, @RequestBody especieDTO: EspecieDTO): EspecieDTO {
    val especie = patogenoService.agregarEspecie(id, especieDTO.nombre, id)
    return EspecieDTO.desdeModelo(especie)
  }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long) = PatogenoDTO.desdeModelo(patogenoService.recuperarPatogeno(id))

  @GetMapping
  fun getAll() = patogenoService.recuperarATodosLosPatogenos().map{ PatogenoDTO.desdeModelo(it)}

  @GetMapping("/especies/{id}")
  fun especiesDePatogeno(@PathVariable id: Long) = patogenoService.especiesDePatogeno(id).map { EspecieDTO.desdeModelo(it) }

  @GetMapping("/esPandemia/{id}")
  fun esPandemia(@PathVariable id: Long) = patogenoService.esPandemia(id)

}

 */