package ar.edu.unq.eperdemic.utils

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
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

    @Autowired
    lateinit var mongoUbicacionDAO: MongoUbicacionDAO

    @Autowired
    lateinit var distritoDAO: DistritoDAO


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
        val ubicacion1Creada = ubicacionService.crearUbicacion(ubicacion1.nombre, GeoJsonPoint(848.0, 834.30))
        val ubicacion2Creada = ubicacionService.crearUbicacion(ubicacion2.nombre, GeoJsonPoint(83452.0, 864.0))
        val ubicacion3Creada = ubicacionService.crearUbicacion(ubicacion3.nombre, GeoJsonPoint(8874.0, 8232.0))
        val ubicacion4Creada = ubicacionService.crearUbicacion("ubicacionPrimera", GeoJsonPoint(826.0, 88.0))
        val ubicacion5Creada = ubicacionService.crearUbicacion("ubicacionSegunda", GeoJsonPoint(883.0, 658.0))
        val ubicacion6Creada = ubicacionService.crearUbicacion("ubicacionTercera", GeoJsonPoint(538.0, 896.560))
        val ubicacion7Creada = ubicacionService.crearUbicacion("ubicacionCuarta", GeoJsonPoint(9868.0, 8568.0))
        val ubicacion8Creada = ubicacionService.crearUbicacion("ubicacionQuinta", GeoJsonPoint(1348.0, 880.0))
        val ubicacion9Creada = ubicacionService.crearUbicacion("ubicacionSexta", GeoJsonPoint(878.0, 834.0))
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

        mongoUbicacionDAO.deleteAll()

        distritoDAO.deleteAll()

    }

}



