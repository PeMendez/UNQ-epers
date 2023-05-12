package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/vector")
class VectorControllerREST(private val vectorService: VectorService) {

    @Autowired
    lateinit var especieService: EspecieService

    @PostMapping("/crear")
    fun create(@RequestBody vector: VectorDTO): VectorDTO {
        val vectorCreado = vectorService.crearVector(vector.tipoDeVector, vector.ubicacion.ubicacionId!!)
        return VectorDTO.desdeModelo(vectorCreado)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = VectorDTO.desdeModelo(vectorService.recuperarVector(id))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = vectorService.borrarVector(id)

    @GetMapping("/vectores")
    fun getAll(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") size:Int): List<VectorDTO> {
        val pageable: Pageable = PageRequest.of(page, size)
        val vectoresPage: Page<Vector> = vectorService.recuperarTodos(pageable)
        return vectoresPage.map { VectorDTO.desdeModelo(it) }.toList()
    }

    @GetMapping("/vectores/{id}")
    fun vectoresEnUbicacion(@PathVariable id: Long) = vectorService.vectoresEnUbicacionID(id).map { VectorDTO.desdeModelo(it) }

    @PutMapping("/contagiar/{vectorId}")
    fun contagiar(@PathVariable vectorId: Long, @RequestBody vectoresDTO:List<VectorDTO>){
        val vector = vectorService.recuperarVector(vectorId)
        val vectores2 = vectoresDTO.map { v ->  vectorService.recuperarVector(v.vectorID!!)}

        vectorService.contagiar(vector,vectores2)
    }

    @PutMapping("/infectar/{vectorId}/{especieId}")
    fun infectar(@PathVariable vectorId: Long, @PathVariable especieId: Long){
        val especie = especieService.recuperarEspecie(especieId)
        val vector = vectorService.recuperarVector(vectorId)

        return vectorService.infectar(vector,especie)
    }

    @GetMapping("/enfermedades/{id}")
    fun enfermedades(@PathVariable id: Long) = vectorService.enfermedades(id).map { EspecieDTO.desdeModelo(it) }

    @ExceptionHandler(NoExisteElid::class)
    fun handleNotFoundException(ex: NoExisteElid): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(InvalidFormatException::class)
    fun handleInvalidFormatException(e: InvalidFormatException): ResponseEntity<String> {
        val expectedValues = e.targetType.enumConstants.joinToString(", ")
        val errorMessage = "Valor inválido para el campo 'tipoDeVector'. Se esperaba uno de los siguientes valores: $expectedValues."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }
}