package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.NingunVectorAInfectarEnLaUbicacionDada
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.jupiter.api.*

class PatogenoServiceTest {

    private val hibernatePatogenoDAO = HibernatePatogenoDAO()
    private val patogenoService = PatogenoServiceImpl(hibernatePatogenoDAO)
    private var dataService = DataServiceHibernate()
    private val hibernateVectorDAO = HibernateVectorDAO()
    private val vectorServiceImpl = VectorServiceImpl(hibernateVectorDAO)
    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieServiceImpl = EspecieServiceImpl(hibernateEspecieDAO)
    private val ubicacionDAO = HibernateUbicacionDAO()
    private val ubicacionService = UbicacionServiceImpl(ubicacionDAO)

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun cuandoSeCreaUnPatogenoSeLeAsignaUnId() {
        var patogenoVirus = Patogeno("Virus")
        patogenoVirus = patogenoService.crearPatogeno(patogenoVirus)

        Assertions.assertNotNull(patogenoVirus.id)
    }

    @Test
    fun recuperarPatogenoTest(){
        val patogenoRecuperado = patogenoService.recuperarPatogeno(1)

        Assertions.assertEquals(patogenoRecuperado.id, 1)
        Assertions.assertEquals(patogenoRecuperado.tipo, "tipo1")
        Assertions.assertEquals(patogenoRecuperado.cantidadDeEspecies, 2)
        Assertions.assertEquals(patogenoRecuperado.capacidadDeContagio, 100)
        Assertions.assertEquals(patogenoRecuperado.capacidadDeBiomecanizacion,100)
        Assertions.assertEquals(patogenoRecuperado.capacidadDeDefensa, 100)
    }

    @Test
    fun seIntentaRecuperarUnPatogenoInexistente(){

        val ex = assertThrows<NoExisteElid> { patogenoService.recuperarPatogeno(10)  }

        Assertions.assertEquals("el id buscado no existe en la base de datos", ex.message)
    }

    @Test
    fun seLeAgregaUnaEspecieAUnPatogenoTest(){
        val especieGenerada = patogenoService.agregarEspecie(1,"EspecieViolenta", 2)
        val listaEspecies = patogenoService.especiesDePatogeno(1)

        Assertions.assertEquals(listaEspecies.size, 3 )
        Assertions.assertEquals(listaEspecies.find { e -> e.id == especieGenerada.id }!!.id, especieGenerada.id)
    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeIncrementaSuCantidadDeEspecies() {
        val patogeno = patogenoService.recuperarPatogeno(1)
        patogenoService.agregarEspecie(1, "virus", 2)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(1)

        Assertions.assertEquals(patogenoRecuperado.cantidadDeEspecies, patogeno.cantidadDeEspecies + 1)
    }

    @Test
    fun seIntentaAgregarUnaEspecieEnUnaUbicacionInexistente(){

        val ex = assertThrows<NoExisteElid> { patogenoService.agregarEspecie(1,"EspecieViolenta", 400)  }

        Assertions.assertEquals("La ubicacion no existe en la base de datos", ex.message)
    }

    @Test
    fun alCrearUnaEspecieSeLeAsignaUnId() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada.id!!)

        Assertions.assertNotNull(especieCreada.id!!)
    }

    @Test
    fun noSePuedeCrearUnaEspecieConNombreVacio() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "", ubicacionCreada.id!!)
        }
    }

    @Test
    fun noSePuedeCrearUnaEspecieConNombreConCaracteresEspeciales() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "", ubicacionCreada.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "especie#1", ubicacionCreada.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "especie--1", ubicacionCreada.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "especie@1", ubicacionCreada.id!!)
        }
    }

    @Test
    fun noSePuedeCrearUnaEspecieConUnPatogenoConIdInvalido() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)

        dataService.eliminarTodo()

        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "unNombreEspecie", ubicacionCreada.id!!)
        }
    }

    @Test
    fun noSePuedeCrearUnaEspecieEnUnaUbicacionSinVectores() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")

        Assertions.assertThrows(NingunVectorAInfectarEnLaUbicacionDada::class.java) {
            patogenoService.agregarEspecie(patogenoCreado.id!!, "unNombreEspecie", ubicacionCreada.id!!)
        }
    }

    @Test
    fun esPandemiaAfirmativo(){
        val unVector = vectorServiceImpl.recuperarVector(6)
        val otroVector = vectorServiceImpl.recuperarVector(7)
        val otroVectormas = vectorServiceImpl.recuperarVector(5)
        val otroVectorcitoMas = vectorServiceImpl.recuperarVector(4)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(otroVector, unaEspecie)
        vectorServiceImpl.infectar(unVector, unaEspecie)
        vectorServiceImpl.infectar(otroVectormas, unaEspecie)
        vectorServiceImpl.infectar(otroVectorcitoMas, unaEspecie)

        Assertions.assertTrue(patogenoService.esPandemia((unaEspecie.id!!)))

    }

    @Test
    fun esPandemiaNegativo(){

        Assertions.assertFalse(patogenoService.esPandemia(1))

    }

    @Test
    fun seRecuperanTodosLosPatogenosExistentes() {

        val listaDePatogenosRecuperados = patogenoService.recuperarATodosLosPatogenos()

        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 1.toLong() })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 2.toLong() })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 3.toLong() })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 4.toLong() })

        Assertions.assertEquals(listaDePatogenosRecuperados.size, 4)

    }

    @Test
    fun siSeIntentanRecuperarTodosLosPatogenosPeroNoHayPatogenosExistentesDevuelveUnaListaVacia() {
        dataService.eliminarTodo()

        Assertions.assertTrue(patogenoService.recuperarATodosLosPatogenos().isEmpty())
    }

    @Test
    fun seIntentanRecuperarLasEspeciesDeUnPatogenoSinEspecies() {
        val listaDeEspeciesRecuperadas = patogenoService.especiesDePatogeno(4)

        Assertions.assertTrue(listaDeEspeciesRecuperadas.isEmpty())
    }

    @Test
    fun seRecuperanTodasLasEspeciesDeUnPatogeno() {

        val listaDeEspeciesRecuperadas = patogenoService.especiesDePatogeno(1)

        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == 1.toLong() })
        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == 6.toLong() })

        Assertions.assertTrue(listaDeEspeciesRecuperadas.size == 2)

    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeInfectaUnVectorEnLaUbicacionDada(){
        val vectorPrevioAInfectarse = vectorServiceImpl.recuperarVector(7)

        Assertions.assertFalse(vectorPrevioAInfectarse.tieneEfermedad(6))

        val especie = patogenoService.agregarEspecie(1, "virus", 8)
        val vector = vectorServiceImpl.recuperarVector(7)

        Assertions.assertTrue(vector.tieneEfermedad(especie.id!!))
    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeIntentaInfectarAUnVectorPeroNoHayNingunoEnLaUbicacionDada(){

        val ex = assertThrows<NingunVectorAInfectarEnLaUbicacionDada> { patogenoService.agregarEspecie(1, "virus", 9)  }

        Assertions.assertEquals("No hay ningún vector en la ubicación dada", ex.message)

    }

    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }

}