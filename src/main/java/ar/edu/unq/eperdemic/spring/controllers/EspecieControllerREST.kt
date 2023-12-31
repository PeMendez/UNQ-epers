package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteUnaEspecieLider
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieLiderDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@ServiceREST
@RequestMapping("/especie")
class EspecieControllerREST(private val especieService: EspecieService){

    @Autowired
    private lateinit var patogeno: PatogenoService

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = EspecieDTO.desdeModelo(especieService.recuperarEspecie(id))

    @GetMapping("/especies")
    fun getAll(@RequestParam("offset", defaultValue = "0") offset: Int, @RequestParam("limit", defaultValue = "10") limit: Int):  List<EspecieDTO> {
        val pageRequest = PageRequest.of(offset, limit)
        return especieService.recuperarTodas(pageRequest).map{EspecieDTO.desdeModelo(it)}.toList()
    }
    @GetMapping("/infectados/{id}")
    fun cantidadDeInfectados(@PathVariable id: Long) = especieService.cantidadDeInfectados(id)

    @GetMapping("/lideres")
    fun lideres() = especieService.lideres().map{EspecieDTO.desdeModelo(it)}

    @GetMapping("/especieLider")
    fun especieLider(): EspecieLiderDTO {
        var especieLider = especieService.especieLider()
        return EspecieLiderDTO.desdeModelo(especieLider.nombre, especieLider.patogeno.tipo,
            especieService.cantidadDeInfectados(especieLider.id!!), patogeno.esPandemia(especieLider.id!!))
    }

    @ExceptionHandler(NoExisteElid::class)
    fun handleNotFoundException(ex: NoExisteElid): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(NoExisteUnaEspecieLider::class)
    fun handleNotFoundException(ex: NoExisteUnaEspecieLider): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

}