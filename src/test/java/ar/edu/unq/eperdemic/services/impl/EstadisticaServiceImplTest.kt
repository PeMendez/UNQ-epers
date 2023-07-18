package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElNombreDeLaUbicacion
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EstadisticaServiceImplTest {

    @Autowired
    private lateinit var dataService: DataService
    @Autowired
    private lateinit var vectorServiceImpl: VectorServiceImpl

    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl

    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl

    @Autowired
    private lateinit var estadisticaService: EstadisticaServiceImpl

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun unaEspecieEsEspecieLiderInfectandoPersonas() {
        dataService.eliminarTodo()

        val paraguay = ubicacionService.crearUbicacion("Paraguay", GeoJsonPoint(8.0, 8.0))
        val virus = Patogeno("Virus")
        val patogeno = patogenoService.crearPatogeno(virus)
        val unVector = vectorServiceImpl.crearVector(TipoDeVector.Persona, paraguay.id!!)
        val especieQueMasInfecto =  patogenoService.agregarEspecie(patogeno.id!!, "enterovirus", paraguay.id!!)

        Assertions.assertEquals(unVector.tipo, TipoDeVector.Persona)
        Assertions.assertEquals(especieQueMasInfecto.id, estadisticaService.especieLider().id)
    }


    @Test
    fun lideresQueInfectaronLaMayorCantidadDeHumanosYAnimales() {
        dataService.eliminarTodo()

        val ubicacion1 = ubicacionService.crearUbicacion("Ubicacion1",GeoJsonPoint(8.0, 8.0))
        val ubicacion2 = ubicacionService.crearUbicacion("Ubicacion2", GeoJsonPoint(8.1, 8.0))
        val ubicacion3 = ubicacionService.crearUbicacion("Chile", GeoJsonPoint(8.2, 8.0))
        val ubicacion4 = ubicacionService.crearUbicacion("Brasil", GeoJsonPoint(8.3, 8.0))
        val ubicacion5 = ubicacionService.crearUbicacion("Uruguay", GeoJsonPoint(8.4, 8.0))
        val ubicacion6 = ubicacionService.crearUbicacion("Paraguay", GeoJsonPoint(8.5, 8.0))
        val ubicacion7 = ubicacionService.crearUbicacion("Venezuela", GeoJsonPoint(8.6, 8.0))
        val ubicacion8 = ubicacionService.crearUbicacion("Irlanda", GeoJsonPoint(8.7, 8.0))
        val ubicacion9 = ubicacionService.crearUbicacion("Japon", GeoJsonPoint(8.8, 8.0))
        val ubicacion10 = ubicacionService.crearUbicacion("China", GeoJsonPoint(8.9, 8.0))
        val ubicacion11 = ubicacionService.crearUbicacion("Korea", GeoJsonPoint(8.0, 8.1))
        val ubicacion12 = ubicacionService.crearUbicacion("Romania", GeoJsonPoint(8.0, 8.2))

        val patogenoModelo = Patogeno("patogeno")
        val patogeno = patogenoService.crearPatogeno(patogenoModelo)

        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion1.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion3.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacion4.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacion5.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion6.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion7.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion8.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion9.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion10.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion11.id!!)
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion12.id!!)

        patogenoService.agregarEspecie(patogeno.id!!, "enterovirus3", ubicacion3.id!!)
        patogenoService.agregarEspecie(patogeno.id!!, "enteroviru4", ubicacion4.id!!)
        patogenoService.agregarEspecie(patogeno.id!!, "enterovirus5", ubicacion5.id!!)
        patogenoService.agregarEspecie(patogeno.id!!, "enterovirus6", ubicacion6.id!!)
        patogenoService.agregarEspecie(patogeno.id!!, "enterovirus7", ubicacion7.id!!)
        patogenoService.agregarEspecie(patogeno.id!!, "enterovirus8", ubicacion8.id!!)
        patogenoService.agregarEspecie(patogeno.id!!, "enterovirus9", ubicacion9.id!!)
        patogenoService.agregarEspecie(patogeno.id!!,"Pap",ubicacion1.id!!)
        val especieChina = patogenoService.agregarEspecie(patogeno.id!!, "enterovirus10", ubicacion10.id!!)
        val especieCoxsackie = patogenoService.agregarEspecie(patogeno.id!!,"Coxsackie",ubicacion1.id!!)

        val mechaVector = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        val roboVector = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        val mechaVectorPlus = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        val roboVectorAdvanced = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)

        vectorServiceImpl.infectar(mechaVector, especieCoxsackie)
        vectorServiceImpl.infectar(roboVector, especieCoxsackie)
        vectorServiceImpl.infectar(mechaVectorPlus, especieCoxsackie)
        vectorServiceImpl.infectar(roboVectorAdvanced, especieCoxsackie)

        Assertions.assertEquals(estadisticaService.lideres().size, 10)
        Assertions.assertEquals(estadisticaService.lideres().first().id, especieCoxsackie.id)
        Assertions.assertEquals(estadisticaService.lideres().last().id, especieChina.id)

    }

    @Test
    fun reporteDeContagios() {
        val patogenoModelo = Patogeno("patogeno")
        val patogeno = patogenoService.crearPatogeno(patogenoModelo)
        val ubicacion1 = ubicacionService.crearUbicacion("Argentina", GeoJsonPoint(8.0, 8.0))
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion1.id!!)
        val especie = patogenoService.agregarEspecie(patogeno.id!!, "Coxsackie", ubicacion1.id!!)

        val reporte = estadisticaService.reporteDeContagios(ubicacion1.nombre)
        Assertions.assertEquals(reporte.vectoresInfectados, 1)
        Assertions.assertEquals(reporte.vectoresPresentes, 1)
        Assertions.assertEquals(reporte.nombreDeEspecieMasInfecciosa, especie.nombre)
    }


    @Test
    fun reporteDeContagiosConUbicacionNoExistente() {
        Assertions.assertThrows(NoExisteElNombreDeLaUbicacion::class.java) {
            estadisticaService.reporteDeContagios("Ubicacion NO EXISTENTE")
        }
    }


    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}
