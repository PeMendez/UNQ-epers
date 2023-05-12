package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElNombreDeLaUbicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.UbicacionDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/ubicacion")
class UbicacionControllerREST(private val ubicacionService: UbicacionService) {

    @GetMapping("/{ubicacionId}")
    fun recuperarUbicacion(@PathVariable ubicacionId: Long) = UbicacionDTO.desdeModelo(ubicacionService.recuperar(ubicacionId))

    @GetMapping("/allUbicaciones")
    fun recuperarTodas(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") size:Int): List<UbicacionDTO> {
        val pageable: Pageable = PageRequest.of(page, size)
        val ubicacionesPage: Page<Ubicacion> = ubicacionService.recuperarTodos(pageable)
        return ubicacionesPage.map { UbicacionDTO.desdeModelo(it)}.toList()

        }

    @GetMapping("/allVectoresInUbicacion/{ubicacionId}")
    fun recuperarVectores(@PathVariable ubicacionId: Long) = ubicacionService.recuperarVectores(ubicacionId).map { vector -> VectorDTO.desdeModelo(vector) }

    @PostMapping
    fun guardarUbicacion(@RequestBody ubicacionDTO: UbicacionDTO) = ubicacionService.guardar(ubicacionDTO.aModelo())

    @PutMapping("/mover/{vectorId}/{ubicacionId}")
    fun mover(@PathVariable vectorId: Long, @PathVariable ubicacionId: Long) = ubicacionService.mover(vectorId, ubicacionId)

    @PutMapping("/expandir/{ubicacionId}")
    fun expandir(@PathVariable ubicacionId: Long) = ubicacionService.expandir(ubicacionId)

    @ExceptionHandler(NoExisteElid::class)
    fun handleNotFoundException(ex: NoExisteElid): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(NoExisteElNombreDeLaUbicacion::class)
    fun handleNotFoundException(ex: NoExisteElNombreDeLaUbicacion): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(NombreDeUbicacionRepetido::class)
    fun handleNotFoundException(ex: NombreDeUbicacionRepetido): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

}