package ar.edu.unq.eperdemic.spring.controllers

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.spring.controllers.dto.EspecieDTO
import ar.edu.unq.eperdemic.spring.controllers.dto.VectorDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@ServiceREST
@RequestMapping("/vector")
class VectorControllerREST(private val vectorService: VectorService) {

    @Autowired
    lateinit var patogenoService: PatogenoService

    @PostMapping
    fun create(@RequestBody tipo: TipoDeVector, ubicacionId: Long): VectorDTO {
        val vector = vectorService.crearVector(tipo,ubicacionId)
        return VectorDTO.desdeModelo(vector)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = VectorDTO.desdeModelo(vectorService.recuperarVector(id))

    @PostMapping("/{id}")
    fun delete(@PathVariable id: Long) = vectorService.borrarVector(id)

    @GetMapping("/vectores")
    fun getAll() = vectorService.recuperarTodos().map{VectorDTO.desdeModelo(it)}

    @GetMapping("/vectores/{id}")
    fun vectoresEnUbicacion(@PathVariable id: Long) = vectorService.vectoresEnUbicacionID(id).map { VectorDTO.desdeModelo(it) }

    @PostMapping("/contagiar")
    fun contagiar(@RequestBody vectorDTO: VectorDTO, vectoresDTO:List<VectorDTO>){
        val vector = vectorDTO.aModelo()
        val vectores = vectoresDTO.map{it.aModelo()}

        vectorService.contagiar(vector,vectores)
    }

    @PostMapping("/infectar")
    fun infectar(@RequestBody vectorDTO: VectorDTO, especieDTO: EspecieDTO){
        val patogeno = patogenoService.recuperarPatogeno(especieDTO.patogenoId!!)
        val vector = vectorDTO.aModelo()
        val especie = especieDTO.aModelo(patogeno)

        vectorService.infectar(vector,especie)
    }

    @GetMapping("/enfermedades/{id}")
    fun enfermedades(@PathVariable id: Long) = vectorService.enfermedades(id).map { EspecieDTO.desdeModelo(it) }
}