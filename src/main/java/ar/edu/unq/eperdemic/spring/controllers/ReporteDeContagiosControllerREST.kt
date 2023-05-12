package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElNombreDeLaUbicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteUnaEspecieLider
import ar.edu.unq.eperdemic.services.EstadisticaService
import ar.edu.unq.eperdemic.spring.controllers.dto.ReporteDeContagiosDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@ServiceREST
@RequestMapping("/reporte")
class ReporteDeContagiosControllerREST(private val estadisticaService: EstadisticaService){

    @GetMapping("/{nombreDeLaUbicacion}")
    fun agregarMutacion(@PathVariable nombreDeLaUbicacion: String): ReporteDeContagiosDTO {
        val reporte = estadisticaService.reporteDeContagios(nombreDeLaUbicacion)
        return ReporteDeContagiosDTO.desdeModelo(reporte, nombreDeLaUbicacion, "LaPelu")
    }

    @ExceptionHandler(NoExisteElNombreDeLaUbicacion::class)
    fun handleNotFoundException(ex: NoExisteElNombreDeLaUbicacion): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(NoExisteUnaEspecieLider::class)
    fun handleNotFoundException(ex: NoExisteUnaEspecieLider): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
}