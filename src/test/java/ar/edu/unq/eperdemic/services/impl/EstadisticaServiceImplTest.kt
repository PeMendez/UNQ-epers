package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElNombreDeLaUbicacion
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

        var paraguay = ubicacionService.crearUbicacion("Paraguay")
        var virus = Patogeno("Virus")
        var patogeno = patogenoService.crearPatogeno(virus)
        val unVector = vectorServiceImpl.crearVector(TipoDeVector.Persona, paraguay.id!!)
        val especieQueMasInfecto =  patogenoService.agregarEspecie(patogeno.id!!, "enterovirus", paraguay.id!!)

        Assertions.assertEquals(unVector.tipo, TipoDeVector.Persona)
        Assertions.assertEquals(especieQueMasInfecto.id, estadisticaService.especieLider().id)
    }


    @Test
    fun lideresQueInfectaronLaMayorCantidadDeHumanosYAnimales() {
        dataService.eliminarTodo()

        var ubicacion1 = ubicacionService.crearUbicacion("Ubicacion1")
        var ubicacion2 = ubicacionService.crearUbicacion("Ubicacion2")
        val ubicacion3 = ubicacionService.crearUbicacion("Chile")
        val ubicacion4 = ubicacionService.crearUbicacion("Brasil")
        val ubicacion5 = ubicacionService.crearUbicacion("Uruguay")
        val ubicacion6 = ubicacionService.crearUbicacion("Paraguay")
        val ubicacion7 = ubicacionService.crearUbicacion("Venezuela")
        val ubicacion8 = ubicacionService.crearUbicacion("Irlanda")
        val ubicacion9 = ubicacionService.crearUbicacion("Japon")
        val ubicacion10 = ubicacionService.crearUbicacion("China")
        val ubicacion11 = ubicacionService.crearUbicacion("Korea")
        val ubicacion12 = ubicacionService.crearUbicacion("Romania")

        var patogenoModelo = Patogeno("patogeno")
        var patogeno = patogenoService.crearPatogeno(patogenoModelo)

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
        var patogenoModelo = Patogeno("patogeno")
        var patogeno = patogenoService.crearPatogeno(patogenoModelo)
        var ubicacion1 = ubicacionService.crearUbicacion("Argentina")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion1.id!!)
        var especie = patogenoService.agregarEspecie(patogeno.id!!, "Coxsackie", ubicacion1.id!!)

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
