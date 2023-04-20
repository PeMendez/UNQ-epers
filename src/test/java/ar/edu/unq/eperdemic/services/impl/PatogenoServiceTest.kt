package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
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

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun cuandoSeCreaUnPatogenoSeLeAsignaUnId() {
        var patogenoVirus = Patogeno("Virus")
        patogenoVirus = patogenoService.crearPatogeno(patogenoVirus)

        Assertions.assertTrue(patogenoVirus.id != null)
    }

    @Test
    fun recuperarPatogenoTest(){
        val patogenoRecuperado = patogenoService.recuperarPatogeno(1)
        val listaEspecies = patogenoService.especiesDePatogeno(1)

        Assertions.assertEquals(patogenoRecuperado.id, 1)
        Assertions.assertEquals(patogenoRecuperado.tipo, "tipo1")
        Assertions.assertEquals(patogenoRecuperado.cantidadDeEspecies, 1)
        Assertions.assertEquals(patogenoRecuperado.capacidadDeContagio, 100)
        Assertions.assertEquals(patogenoRecuperado.capacidadDeBiomecanizacion,100)
        Assertions.assertEquals(patogenoRecuperado.capacidadDeDefensa, 100)
    }

    // falta test cuando el id no es correcto

    @Test
    fun seLeAgregaUnaEspecieAUnPatogenoTest(){
        val especieGenerada = patogenoService.agregarEspecie(1,"EspecieViolenta", 2)
        val listaEspecies = patogenoService.especiesDePatogeno(1)

        Assertions.assertEquals(listaEspecies.size, 2 )
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
    fun esPandemiaAfirmativo(){
        val unVector = vectorServiceImpl.recuperarVector(1)
        val otroVector = vectorServiceImpl.recuperarVector(2)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)
        val otraEspecie = especieServiceImpl.recuperarEspecie(2)

        vectorServiceImpl.infectar(otroVector, unaEspecie)
        vectorServiceImpl.infectar(otroVector, otraEspecie)
        vectorServiceImpl.infectar(unVector, unaEspecie)

        Assertions.assertTrue(patogenoService.esPandemia((unaEspecie.id!!)))

    }

    @Test
    fun esPandemiaNegativo(){
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        Assertions.assertFalse(patogenoService.esPandemia(unaEspecie.id!!))

    }
    @Test
    fun seRecuperanTodosLosPatogenosExistentes() {

        val listaDePatogenosRecuperados = patogenoService.recuperarATodosLosPatogenos()

        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 1.toLong() })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 2.toLong() })
        Assertions.assertNotNull(listaDePatogenosRecuperados.find { it.id == 3.toLong() })

        Assertions.assertTrue(listaDePatogenosRecuperados.size == 3)

    }


    @Test
    fun seRecuperanTodasLasEspeciesDeUnPatogeno() {

        val listaDeEspeciesRecuperadas = patogenoService.especiesDePatogeno(2)

        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == 2.toLong() })
        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == 4.toLong() })
        Assertions.assertNotNull(listaDeEspeciesRecuperadas.find { it.id == 5.toLong() })

        Assertions.assertTrue(listaDeEspeciesRecuperadas.size == 3)

    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeInfectaUnVectorEnLaUbicacionDada(){
        val vector = vectorServiceImpl.recuperarVector(7)
        val especie = patogenoService.agregarEspecie(1, "virus", 2)

        Assertions.assertTrue(vector.tieneEfermedad(especie.id!!))
    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeIntentaInfectarAUnVectorPeroNoHayNingunoEnLaUbicacionDada(){

    }

    /*
    * testear que cuando se crea una especie se infecta un vector.
    * testear el error que no hay vector en la ubiación
    * */



    //@AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }

}