package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MutacionServiceImplTest {

    @Autowired
    private lateinit var mutacionService: MutacionServiceImpl
    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl
    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl
    @Autowired
    private lateinit var vectorService: VectorServiceImpl
    @Autowired
    private lateinit var especieService: EspecieServiceImpl
    @Autowired
    private lateinit var dataService: DataService

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun cuandoSeCreaUnaMutacionSeLeAsignaUnId() {

        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie1", GeoJsonPoint(8.0, 8.0))
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar = Mutacion()

        val mutacionPersistida = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar)

        val especieRec = especieService.recuperarEspecie(especieCreada.id!!)


        assertTrue(especieRec.mutaciones.size == 1)
        assertNotNull(mutacionPersistida.id)
    }

    @Test
    fun seRecuperaUnaMutacionConTodosSusDatosCorrectos() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie1",GeoJsonPoint(8.0, 8.0))
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar = Mutacion(TipoDeMutacion.SupresionBiomecanica)

        val mutacionPersistida = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar)
        val mutacionRecuperada = mutacionService.recuperarMutacion(mutacionPersistida.id!!)

        assertEquals(mutacionPersistida.id!!, mutacionRecuperada.id!!)
        assertEquals(mutacionPersistida.potenciaDeMutacion, mutacionRecuperada.potenciaDeMutacion)
        assertEquals(mutacionPersistida.tipoDeMutacion, mutacionRecuperada.tipoDeMutacion)
        assertEquals(mutacionPersistida.tipoDeVector, mutacionRecuperada.tipoDeVector)
        assertEquals(mutacionPersistida.especie.id!!, mutacionRecuperada.especie.id!!)
    }

    @Test
    fun noSePuedeRecuperarUnaMutacionConUnIdInexistente() {
        assertThrows(NoExisteElid::class.java) {
            mutacionService.recuperarMutacion(-111)
        }
    }

    @Test
    fun seRecuperanTodasLasMutacionesCorrectamente() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie1", GeoJsonPoint(8.0, 8.0))
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion()
        val mutacion1 = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar1)

        val mutaciones1 = mutacionService.recuperarTodas()

        val mutacionAAgregar2 = Mutacion()
        val mutacion2 = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar2)

        val mutaciones2 = mutacionService.recuperarTodas()

        assertTrue(mutaciones1.size == 1)
        assertTrue(mutaciones2.size == 2)

        assertNotNull(mutaciones2.find { it.id == mutacion1.id!!})
        assertNotNull(mutaciones2.find { it.id == mutacion2.id!!})
    }

    @Test
    fun alRecuperarTodasLasMutacionesDeUnaBDDVaciaEntoncesSeRetornaUnaListaVacia() {
        dataService.eliminarTodo()
        val mutaciones = mutacionService.recuperarTodas()

        assertTrue(mutaciones.isEmpty())
    }

    @Test
    fun seIntentaAgregarUnaMutacionAUnaEspecieInexistenteYRompe() {
        val mutacionTest = Mutacion()
        assertThrows(NoExisteElid::class.java) {
            mutacionService.agregarMutacion(-11, mutacionTest)
        }
    }

    @Test
    fun seRecuperanTodasLasMutacionesDeManeraCorrectaConPaginaUnoYSizeDos() {
        dataService.eliminarTodo()

        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie1", GeoJsonPoint(8.0, 8.0))
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion()
        mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar1)

        val mutacionAAgregar2 = Mutacion()
        mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar2)

        val mutacionAAgregar3 = Mutacion()
        mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar3)


        val pageable = PageRequest.of(1, 2)

        val mutacionesRecuperadas = mutacionService.recuperarTodas(pageable)


        assertEquals(1, mutacionesRecuperadas.number)
        assertEquals(1, mutacionesRecuperadas.numberOfElements)
    }

    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}