package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import org.springframework.web.bind.annotation.*


@CrossOrigin
@ServiceREST
@RequestMapping("/especie")
class EspecieControllerREST(private val especieService: EspecieService){

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = EspecieDTO.desdeModelo(especieService.recuperarEspecie(id))

    @GetMapping
    fun getAll() = especieService.recuperarTodas().map{ EspecieDTO.desdeModelo(it)}

    @GetMapping("/infectados/{id}")
    fun cantidadDeInfectados(@PathVariable id: Long) = especieService.cantidadDeInfectados(id)

    @GetMapping("/lideres")
    fun lideres() = especieService.lideres().map{EspecieDTO.desdeModelo(it)}

    @GetMapping("/especieLider")
    fun especieLider() = EspecieDTO.desdeModelo(especieService.especieLider())


}