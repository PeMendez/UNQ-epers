package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.NingunVectorAInfectarEnLaUbicacionDada
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatogenoServiceTest {


    @Autowired
    private lateinit var vectorServiceImpl: VectorServiceImpl

    @Autowired
    private lateinit var dataService: DataService
    @Autowired
    private lateinit var vectorService: VectorServiceImpl
    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl
    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl
    private var patogenoVirusCruciartus = Patogeno("VirusCruciartus")
    private lateinit var patogenoMalDeDragon: Patogeno
    private lateinit var ubicacionPrivateDrive: Ubicacion
    private lateinit var ubicacionLaMadriguera: Ubicacion
    private lateinit var vectorHarryPotter: Vector
    private lateinit var vectorRonWeasley: Vector


    @BeforeEach
    fun setUp() {
        Random.switchModo(false)
        dataService.crearSetDeDatosIniciales()
        patogenoMalDeDragon = patogenoService.crearPatogeno(Patogeno("MalDeDragon"))
        ubicacionPrivateDrive = ubicacionService.crearUbicacion("PrivateDrive")
        vectorHarryPotter = vectorService.crearVector(TipoDeVector.Persona, ubicacionPrivateDrive.id!!)
        ubicacionLaMadriguera = ubicacionService.crearUbicacion("ubicacionLaMadriguera")

    }

    @Test
    fun cuandoSeCreaUnPatogenoSeLeAsignaUnId() {
        val patogenoVirus = patogenoService.crearPatogeno(patogenoVirusCruciartus)

        Assertions.assertNotNull(patogenoVirus.id)
    }

    @Test
    fun recuperarPatogenoTest(){

        val patogenoVirus = patogenoService.crearPatogeno(patogenoVirusCruciartus)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogenoVirus.id!!)

        Assertions.assertEquals(patogenoVirus.id, patogenoRecuperado.id)
        Assertions.assertEquals(patogenoVirus.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogenoVirus.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)
        Assertions.assertEquals(patogenoVirus.capacidadDeContagio, patogenoRecuperado.capacidadDeContagio)
        Assertions.assertEquals(patogenoVirus.capacidadDeBiomecanizacion,patogenoRecuperado.capacidadDeBiomecanizacion)
        Assertions.assertEquals(patogenoVirus.capacidadDeDefensa, patogenoRecuperado.capacidadDeDefensa)
    }

    @Test
    fun seIntentaRecuperarUnPatogenoInexistente(){

        val ex = assertThrows<NoExisteElid> { patogenoService.recuperarPatogeno(-10)  }

        Assertions.assertEquals("el id buscado no existe en la base de datos", ex.message)
    }

    @Test
    fun seLeAgregaUnaEspecieAUnPatogenoTest(){
        val especieGenerada = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!,"EspecieImperius", ubicacionPrivateDrive.id!!)
        val listaEspecies = patogenoService.especiesDePatogeno(patogenoMalDeDragon.id!!)

        Assertions.assertEquals(1, listaEspecies.size)
        Assertions.assertEquals(listaEspecies.find { e -> e.id == especieGenerada.id }!!.id, especieGenerada.id)
    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeIncrementaSuCantidadDeEspecies() {
        val patogeno = patogenoService.recuperarPatogeno(patogenoMalDeDragon.id!!)
        patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "EspecieImperius", ubicacionPrivateDrive.id!!)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogenoMalDeDragon.id!!)

        Assertions.assertEquals(patogenoRecuperado.cantidadDeEspecies, patogeno.cantidadDeEspecies + 1)
    }

    @Test
    fun seIntentaAgregarUnaEspecieEnUnaUbicacionInexistente(){

        val ex = assertThrows<NoExisteElid> { patogenoService.agregarEspecie(patogenoMalDeDragon.id!!,"EspecieImperius", 400)  }

        Assertions.assertEquals("No existe la ubicacion", ex.message)
    }

    @Test
    fun alCrearUnaEspecieSeLeAsignaUnId() {
        val especieCreada = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "EspecieImperius", ubicacionPrivateDrive.id!!)

        Assertions.assertNotNull(especieCreada.id!!)
    }

    @Test
    fun noSePuedeCrearUnaEspecieConNombreVacio() {
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "", ubicacionPrivateDrive.id!!)
        }
    }

    @Test
    fun noSePuedeCrearUnaEspecieConNombreConCaracteresEspeciales() {

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "especie#1", ubicacionPrivateDrive.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "especie--1", ubicacionPrivateDrive.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "especie@1", ubicacionPrivateDrive.id!!)
        }
    }

    @Test
    fun noSePuedeCrearUnaEspecieConUnPatogenoConIdInvalido() {
        dataService.eliminarTodo()

        val ubicacionHogsmeade = ubicacionService.crearUbicacion("ubicacionHogsmeade")
        vectorService.crearVector(TipoDeVector.Persona, ubicacionHogsmeade.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            patogenoService.agregarEspecie(-10, "unNombreEspecie", ubicacionHogsmeade.id!!)
        }
    }

    @Test
    fun noSePuedeCrearUnaEspecieEnUnaUbicacionSinVectores() {
        Assertions.assertThrows(NingunVectorAInfectarEnLaUbicacionDada::class.java) {
            patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "EspecieImperius", ubicacionLaMadriguera.id!!)
        }
    }

    @Test
    fun esPandemiaAfirmativo(){
        dataService.eliminarTodo()
        patogenoMalDeDragon = patogenoService.crearPatogeno(Patogeno("MalDeDragon"))
        ubicacionPrivateDrive = ubicacionService.crearUbicacion("PrivateDrive")
        vectorHarryPotter = vectorService.crearVector(TipoDeVector.Persona, ubicacionPrivateDrive.id!!)
        ubicacionLaMadriguera = ubicacionService.crearUbicacion("ubicacionLaMadriguera")
        vectorRonWeasley = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionLaMadriguera.id!!)
        val especieImperius = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "EspecieImperius", ubicacionPrivateDrive.id!!)

        vectorServiceImpl.infectar(vectorRonWeasley, especieImperius)

        Assertions.assertTrue(patogenoService.esPandemia((especieImperius.id!!)))

    }

    @Test
    fun esPandemiaNegativo(){
        val especieImperius = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "EspecieImperius", ubicacionPrivateDrive.id!!)
        Assertions.assertFalse(patogenoService.esPandemia(especieImperius.id!!))

    }

    @Test
    fun seRecuperanTodosLosPatogenosExistentes() {
        dataService.eliminarTodo()
        patogenoMalDeDragon = patogenoService.crearPatogeno(Patogeno("MalDeDragon"))
        patogenoVirusCruciartus = patogenoService.crearPatogeno(Patogeno("VirusCruciartus"))
        var patogenoAvadaKedavra = patogenoService.crearPatogeno(Patogeno("AvadaKedavra"))

        val listaDePatogenosRecuperados = patogenoService.recuperarATodosLosPatogenos()

        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == patogenoMalDeDragon.id })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == patogenoVirusCruciartus.id })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == patogenoAvadaKedavra.id })

        Assertions.assertEquals(3, listaDePatogenosRecuperados.size)

    }

    @Test
    fun siSeIntentanRecuperarTodosLosPatogenosPeroNoHayPatogenosExistentesDevuelveUnaListaVacia() {
        dataService.eliminarTodo()

        Assertions.assertTrue(patogenoService.recuperarATodosLosPatogenos().isEmpty())
    }

    @Test
    fun seIntentanRecuperarLasEspeciesDeUnPatogenoSinEspecies() {

        val listaDeEspeciesRecuperadas = patogenoService.especiesDePatogeno(patogenoMalDeDragon.id!!)

        Assertions.assertTrue(listaDeEspeciesRecuperadas.isEmpty())
    }

    @Test
    fun seRecuperanTodasLasEspeciesDeUnPatogeno() {
        val especieImperius = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "Imperius", ubicacionPrivateDrive.id!!)
        val especieCruciartus = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "Cruciartus", ubicacionPrivateDrive.id!!)

        val listaDeEspeciesRecuperadas = patogenoService.especiesDePatogeno(patogenoMalDeDragon.id!!)

        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == especieImperius.id })
        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == especieCruciartus.id })

        Assertions.assertTrue(listaDeEspeciesRecuperadas.size == 2)

    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeInfectaUnVectorEnLaUbicacionDada(){

        Assertions.assertTrue(vectorHarryPotter.estaSano())

        val especieImperius = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "Imperius", ubicacionPrivateDrive.id!!)

        val vectorHarryPotterEnfermo = vectorServiceImpl.recuperarVector(vectorHarryPotter.id!!)

        Assertions.assertTrue(vectorHarryPotterEnfermo.tieneEfermedad(especieImperius.id!!))
    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeIntentaInfectarAUnVectorPeroNoHayNingunoEnLaUbicacionDada(){
        val ubicacionHogwarts = ubicacionService.crearUbicacion("Hogwarts")

        val ex = assertThrows<NingunVectorAInfectarEnLaUbicacionDada> { patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "Imperius", ubicacionHogwarts.id!!)  }

        Assertions.assertEquals("No hay ningún vector en la ubicación dada", ex.message)

    }

    @Test
    fun seRecuperanTodasLasEspeciesDeUnPatogenoConPaginaCeroYSizeUno() {
        val especieImperius = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "Imperius", ubicacionPrivateDrive.id!!)
        val especieCruciartus = patogenoService.agregarEspecie(patogenoMalDeDragon.id!!, "Cruciartus", ubicacionPrivateDrive.id!!)

        val listaDeEspeciesRecuperadas = patogenoService.especiesDePatogeno(patogenoMalDeDragon.id!!)


        val pageable = PageRequest.of(0, 1)

        val patogenosRecuperados = patogenoService.recuperarATodosLosPatogenos(pageable)


        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == especieImperius.id })
        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == especieCruciartus.id })
        Assertions.assertTrue(listaDeEspeciesRecuperadas.size == 2)

        Assertions.assertEquals(0, patogenosRecuperados.number)
        Assertions.assertEquals(1, patogenosRecuperados.numberOfElements)
    }


    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }

}

