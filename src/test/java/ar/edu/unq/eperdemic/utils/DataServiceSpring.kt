package ar.edu.unq.eperdemic.utils

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.UbicacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import org.springframework.data.neo4j.repository.query.Query


@Service
class DataServiceSpring : DataService {

    @Autowired
    lateinit var ubicacionDAO: UbicacionDAO
    @Autowired
    lateinit var vectorDAO: VectorDAO
    @Autowired
    lateinit var patogenoDAO: PatogenoDAO
    @Autowired
    lateinit var especieDAO: EspecieDAO
    @Autowired
    lateinit var mutacionDAO: MutacionDAO

    @Autowired
    lateinit var patogenoService: PatogenoServiceImpl
    @Autowired
    lateinit var ubicacionService: UbicacionServiceImpl
    @Autowired
    lateinit var vectorService: VectorServiceImpl

    @Autowired
    lateinit var neo4jUbicacionDAO: Neo4jUbicacionDAO


    val patogeno1 = Patogeno("tipo1")
    val patogeno2 = Patogeno("tipo2")
    val patogeno3 = Patogeno("tipo3")
    val patogeno4 = Patogeno("tipo4")
    val ubicacion1 = Ubicacion("ubicacionRara")
    val ubicacion2 = Ubicacion("ubicacionNueva")
    val ubicacion3 = Ubicacion("ubicacionTesteable")


    override fun crearSetDeDatosIniciales() {
        Random.switchModo(false)
        val listaPatogenosCreados = crearPatogenos()
        val listaDeUbicacionesCreadas = crearUbicaciones()
        crearVectores(listaDeUbicacionesCreadas)

        crearEspecies(listaPatogenosCreados, listaDeUbicacionesCreadas)

    }


    private fun crearPatogenos(): List<Patogeno> {
        val patogeno1Creado = patogenoService.crearPatogeno(patogeno1)
        val patogeno2Creado = patogenoService.crearPatogeno(patogeno2)
        val patogeno3Creado = patogenoService.crearPatogeno(patogeno3)
        val patogeno4Creano = patogenoService.crearPatogeno(patogeno4)
        return listOf(patogeno1Creado, patogeno2Creado, patogeno3Creado, patogeno4Creano)
    }

    private fun crearUbicaciones(): List<Ubicacion> {
        val ubicacion1Creada = ubicacionService.crearUbicacion(ubicacion1.nombre)
        val ubicacion2Creada = ubicacionService.crearUbicacion(ubicacion2.nombre)
        val ubicacion3Creada = ubicacionService.crearUbicacion(ubicacion3.nombre)
        val ubicacion4Creada = ubicacionService.crearUbicacion("ubicacionPrimera")
        val ubicacion5Creada = ubicacionService.crearUbicacion("ubicacionSegunda")
        val ubicacion6Creada = ubicacionService.crearUbicacion("ubicacionTercera")
        val ubicacion7Creada = ubicacionService.crearUbicacion("ubicacionCuarta")
        val ubicacion8Creada = ubicacionService.crearUbicacion("ubicacionQuinta")
        val ubicacion9Creada = ubicacionService.crearUbicacion("ubicacionSexta")
        return listOf(
            ubicacion1Creada, ubicacion2Creada, ubicacion3Creada,
            ubicacion4Creada, ubicacion5Creada, ubicacion6Creada,
            ubicacion7Creada, ubicacion8Creada, ubicacion9Creada
        )
    }

    private fun crearVectores(listaDeUbicaciones: List<Ubicacion>) {
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Insecto, listaDeUbicaciones[1].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[6].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[7].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[1].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[6].id!!)

    }

    private fun crearEspecies(listaDePatogenos: List<Patogeno>, listaDeUbicaciones: List<Ubicacion>) {
        patogenoService.agregarEspecie(listaDePatogenos[0].id!!, "especieASD", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie2", listaDeUbicaciones[1].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[2].id!!, "especie3", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie4", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie5", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[0].id!!, "especie6", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie7", listaDeUbicaciones[1].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[2].id!!, "especie8", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie9", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie10", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie11", listaDeUbicaciones[2].id!!)

    }


    @Transactional
    override fun eliminarTodo() {
        ubicacionDAO.deleteAll()
        vectorDAO.deleteAll()
        patogenoDAO.deleteAll()
        especieDAO.deleteAll()
        mutacionDAO.deleteAll()

        neo4jUbicacionDAO.detachDelete()
    }

}



