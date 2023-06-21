package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.exceptions.*
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.spring.controllers.dto.UbicacionDTO
import org.springframework.data.domain.PageRequest
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
    fun recuperarTodas(@RequestParam("offset", defaultValue = "0") offset: Int, @RequestParam("limit", defaultValue = "10") limit: Int): List<UbicacionDTO> {
        val pageRequest = PageRequest.of(offset, limit)
        return  ubicacionService.recuperarTodos(pageRequest).map{ UbicacionDTO.desdeModelo(it)}.toList()
    }

    //@PostMapping
    //fun guardarUbicacion(@RequestBody ubicacionDTO: UbicacionDTO) = ubicacionService.crearUbicacion(ubicacionDTO.nombreDeLaUbicacion)

    @PutMapping("/mover/{vectorId}/{ubicacionId}")
    fun mover(@PathVariable vectorId: Long, @PathVariable ubicacionId: Long) = ubicacionService.mover(vectorId, ubicacionId)

    @PutMapping("/expandir/{ubicacionId}")
    fun expandir(@PathVariable ubicacionId: Long) = ubicacionService.expandir(ubicacionId)

    @PostMapping("/conectar/{nombreUbicacion1}/{nombreUbicacion2}/{tipoCamino}")
    fun conectar(@PathVariable nombreUbicacion1: String, @PathVariable nombreUbicacion2: String, @PathVariable tipoCamino: String){
        ubicacionService.conectar(nombreUbicacion1, nombreUbicacion2, tipoCamino)
    }

    @GetMapping("/conectados/{nombreUbicacion}")
    fun conectados(@PathVariable nombreUbicacion: String) = ubicacionService.conectados(nombreUbicacion)

    @PutMapping("/moverMasCorto/{vectorId}/{nombreUbicacion}")
    fun moverMasCorto(@PathVariable vectorId: Long, @PathVariable nombreUbicacion: String) = ubicacionService.moverMasCorto(vectorId, nombreUbicacion)

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

    @ExceptionHandler(UbicacionMuyLejana::class)
    fun handleNotFoundException(ex: UbicacionMuyLejana): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(UbicacionNoAlcanzable::class)
    fun handleNotFoundException(ex: UbicacionNoAlcanzable): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
    @ExceptionHandler(TipoDeCaminoInvalido::class)
    fun handleNotFoundException(ex: TipoDeCaminoInvalido): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

}