package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.UbicacionDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@CrossOrigin
@ServiceREST
@RequestMapping("/ubicacion")
class UbicacionControllerREST(private val ubicacionService: UbicacionService) {

    @GetMapping("/{ubicacionId}")
    fun recuperarUbicacion(@PathVariable ubicacionId: Long) = UbicacionDTO.desdeModelo(ubicacionService.recuperar(ubicacionId))

    @GetMapping("/allUbicaciones")
    fun recuperarTodas() = ubicacionService.recuperarTodos().map { ubicacion -> UbicacionDTO.desdeModelo(ubicacion) }

    @GetMapping("/allVectoresInUbicacion/{ubicacionId}")
    fun recuperarVectores(@PathVariable ubicacionId: Long) = ubicacionService.recuperarVectores(ubicacionId).map { vector -> VectorDTO.desdeModelo(vector) }



    @PostMapping
    fun create(@RequestBody ubicacion: Ubicacion): UbicacionDTO {
        val ubicacionCreada = ubicacionService.crearUbicacion(ubicacion.nombre)
        return UbicacionDTO.desdeModelo(ubicacionCreada)
    }


    /*
    @PostMapping
    fun guardarUbicacion(@RequestBody ubicacionDTO: UbicacionDTO) = ubicacionService.guardar(ubicacionDTO.aModelo())*/


    @PutMapping("/mover/{vectorId}/{ubicacionId}")
    fun mover(@PathVariable vectorId: Long, @PathVariable ubicacionId: Long) = ubicacionService.mover(vectorId, ubicacionId)

    @PutMapping("/expandir/{ubicacionId}")
    fun expandir(@PathVariable ubicacionId: Long) = ubicacionService.expandir(ubicacionId)

}