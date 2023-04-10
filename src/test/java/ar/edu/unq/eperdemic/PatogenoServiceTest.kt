package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.*

class PatogenoServiceTest {

    lateinit var patogeno: Patogeno
    lateinit var patogenoBacteria: Patogeno
    lateinit var virus: Patogeno
    private val hibernatePatogenoDAO = HibernatePatogenoDAO()
    private val patogenoService = PatogenoServiceImpl(hibernatePatogenoDAO)

    @BeforeEach
    fun crearModelo() {
        patogeno = Patogeno("Virus")
        patogenoBacteria = Patogeno("Bacteria")
    }

    @Test
    fun crearPatogenoTest() {
        var patogeno = Patogeno("test-tipo")
        patogeno = patogenoService.crearPatogeno(patogeno)
        Assert.assertTrue(patogeno.id != null)
    }

    /*
    @Test
    fun elPatogenoCreadoAhoraTieneUnIdAsignado() {
        patogenoService.crearPatogeno(patogenoBacteria)
        Assertions.assertNotNull(patogenoBacteria.id)
    }

    @Test
    fun seCreaUnPatogenoEnLaBaseDeDatos() {
        patogenoService.crearPatogeno(patogeno)
        var patogenoRecuperado :Patogeno = patogenoService.recuperarPatogeno(patogeno.id!!.toLong())

        //un vez recuperado se crea un objeto de caracteristicas iguales
        Assertions.assertEquals(patogeno.id, patogenoRecuperado.id)
        Assertions.assertEquals(patogeno.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogeno.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)

        //pero no es el mismo objeto
        Assertions.assertFalse(patogeno===patogenoRecuperado)
    }

    @Test
    fun actualizarPatogenoExistente() {
        // Armamos el patogeno virus
        virus = Patogeno("Virus")

        // Modificamos los datos del patógeno una vez creado, teniendo el ID que se le asigno
        var patogenoConId :Patogeno = patogenoService.crearPatogeno(virus)
        patogenoConId.tipo = "Bacteria"
        patogenoConId.cantidadDeEspecies = 524

        // Actualizamos el patógeno en la base de datos
        patogenoService.actualizarPatogeno(patogenoConId)

        // Recuperamos el patógeno de la base de datos
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogenoConId.id!!)

        // Verificamos que los datos hayan sido actualizados correctamente
        Assertions.assertEquals(patogenoConId.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogenoConId.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)
    }


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

    @Test
    fun agregarEspecie() {
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogenoCreado.id!!, "virus", "Argentina")
        val patogenoRecuperado = patogenoService.recuperarPatogeno(patogenoCreado.id!!)

        Assertions.assertEquals(patogenoRecuperado.cantidadDeEspecies, patogenoCreado.cantidadDeEspecies + 1)
    }
*/
    /*
    @AfterEach
    fun eliminarModelo() {
        dataDAO.eliminarTodo()
    }
    */
}