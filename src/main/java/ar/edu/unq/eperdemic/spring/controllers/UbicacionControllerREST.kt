package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.services.UbicacionService
//import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@CrossOrigin
@ServiceREST
@RequestMapping("/ubicacion")
class UbicacionControllerREST(private val ubicacionService: UbicacionService) {

}