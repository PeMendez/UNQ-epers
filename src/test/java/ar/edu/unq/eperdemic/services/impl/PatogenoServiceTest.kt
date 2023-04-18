package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Diosito
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.jupiter.api.*

class PatogenoServiceTest {

    private lateinit var patogenoVirus: Patogeno
    private lateinit var patogenoBacteria: Patogeno
    private val hibernatePatogenoDAO = HibernatePatogenoDAO()
    private val patogenoService = PatogenoServiceImpl(hibernatePatogenoDAO)
    var dataService = DataServiceHibernate()
    private val hibernateVectorDAO = HibernateVectorDAO()
    private val vectorServiceImpl = VectorServiceImpl(hibernateVectorDAO)
    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieServiceImpl = EspecieServiceImpl(hibernateEspecieDAO)
    private val diosito = Diosito

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
        patogenoVirus = Patogeno("Virus")
        patogenoBacteria = Patogeno("Bacteria")
        diosito.switchModo(false)
    }

    @Test
    fun cuandoSeCreaUnPatogenoSeLeAsignaUnId() {
        patogenoVirus = patogenoService.crearPatogeno(patogenoVirus)
        Assertions.assertTrue(patogenoVirus.id != null)
    }

    @Test
    fun recuperarPatogenoTest(){
        val patogenoRecuperado = patogenoService.crearPatogeno(patogenoVirus)
        Assertions.assertEquals(patogenoVirus.id, patogenoRecuperado.id)
        Assertions.assertEquals(patogenoVirus.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogenoVirus.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)
        Assertions.assertEquals(patogenoVirus.capacidadDeContagio, patogenoRecuperado.capacidadDeContagio)
        Assertions.assertEquals(patogenoVirus.capacidadDeBiomecanizacion, patogenoRecuperado.capacidadDeBiomecanizacion)
    }

    @Test
    fun seLeAgregaUnaEspecieAUnPatogenoTest(){
        patogenoService.crearPatogeno(patogenoBacteria)
        val especieGenerada = patogenoService.agregarEspecie(patogenoBacteria.id!!,"EspecieViolenta", 2)
        val listaEspecies = patogenoService.especiesDePatogeno(patogenoBacteria.id!!)
        Assertions.assertTrue(listaEspecies.size == 1 )
        Assertions.assertEquals(listaEspecies.find { e -> e.id == especieGenerada.id }!!.id, especieGenerada.id)
    }

    @Test
    fun alAgregarleUnaEspecieAUnPatogenoSeIncrementaSuCantidadDeEspecies() {
        val patogenoCreado = patogenoService.crearPatogeno(patogenoBacteria)
        patogenoService.agregarEspecie(patogenoCreado.id!!, "virus", 2)
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogenoCreado.id!!)
        Assertions.assertEquals(patogenoRecuperado.cantidadDeEspecies, patogenoCreado.cantidadDeEspecies + 1)
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
        val unVector = vectorServiceImpl.recuperarVector(1)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)
        val otraEspecie = especieServiceImpl.recuperarEspecie(2)
        vectorServiceImpl.infectar(unVector, unaEspecie)
        vectorServiceImpl.infectar(unVector, otraEspecie)

        Assertions.assertFalse(patogenoService.esPandemia(unaEspecie.id!!))

    }


    /*

    @Test
    fun seRecuperanTodosLosPatogenosExistentes() {
        // Se crea una lista con dos patogenos en el orden que se espera esten cuando se recuperen
        val listaDePatogenos : List<Patogeno> = listOf(patogenoBacteria, patogeno)
        // Se crean patogenos y se persisten
        patogenoService.crearPatogeno(patogeno)
        patogenoService.crearPatogeno(patogenoBacteria)
        // Se recuperan todos los patogenos
        val listaPatogenosRecuperados : List<Patogeno> = patogenoService.recuperarATodosLosPatogenos()
        // Se controla que el orden y los patogenos sean los mismos
        Assertions.assertEquals(listaDePatogenos[0].id, listaPatogenosRecuperados[0].id)
        Assertions.assertEquals(listaDePatogenos[0].tipo, listaPatogenosRecuperados[0].tipo)
        Assertions.assertEquals(listaDePatogenos[0].cantidadDeEspecies, listaPatogenosRecuperados[0].cantidadDeEspecies)
        Assertions.assertEquals(listaDePatogenos[1].id, listaPatogenosRecuperados[1].id)
        Assertions.assertEquals(listaDePatogenos[1].tipo, listaPatogenosRecuperados[1].tipo)
        Assertions.assertEquals(listaDePatogenos[1].cantidadDeEspecies, listaPatogenosRecuperados[1].cantidadDeEspecies)
    }
*/

    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }

}