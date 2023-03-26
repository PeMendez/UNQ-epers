package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCDataDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PatogenoServiceTest {
    private val dao: PatogenoDAO = JDBCPatogenoDAO()
    private val dataDAO: DataDAO = JDBCDataDAO()
    lateinit var patogeno: Patogeno
    lateinit var patogenoBacteria: Patogeno
    lateinit var virus: Patogeno

    @BeforeEach
    fun crearModelo() {
        patogeno = Patogeno("Virus")
        patogenoBacteria = Patogeno("Bacteria")

    }

    @Test
    fun elPatogenoCreadoAhoraTieneUnIdAsignado() {
        dao.crear(patogenoBacteria)
        Assertions.assertNotNull(patogenoBacteria.id)
    }

    @Test
    fun seCreaUnPatogenoEnLaBaseDeDatos() {
        dao.crear(patogeno)
        var patogenoRecuperado :Patogeno = dao.recuperar(patogeno.id!!.toLong())

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
        var patogenoConId :Patogeno = dao.crear(virus)
        patogenoConId.tipo = "Bacteria"
        patogenoConId.cantidadDeEspecies = 524

        // Actualizamos el patógeno en la base de datos
        dao.actualizar(patogenoConId)

        // Recuperamos el patógeno de la base de datos
        val patogenoRecuperado = dao.recuperar(patogenoConId.id!!)

        // Verificamos que los datos hayan sido actualizados correctamente
        Assertions.assertEquals(patogenoConId.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogenoConId.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)
    }


    @Test
    fun seRecuperanTodosLosPatogenosExistentes() {
        // Se crea una lista con dos patogenos en el orden que se espera esten cuando se recuperen
        val listaDePatogenos : List<Patogeno> = listOf(patogenoBacteria, patogeno)

        // Se crean patogenos y se persisten
        dao.crear(patogeno)
        dao.crear(patogenoBacteria)

        // Se recuperan todos los patogenos
        val listaPatogenosRecuperados : List<Patogeno> = dao.recuperarATodos()

        // Se controla que el orden y los patogenos sean los mismos
        Assertions.assertEquals(listaDePatogenos[0].id, listaPatogenosRecuperados[0].id)
        Assertions.assertEquals(listaDePatogenos[0].tipo, listaPatogenosRecuperados[0].tipo)
        Assertions.assertEquals(listaDePatogenos[0].cantidadDeEspecies, listaPatogenosRecuperados[0].cantidadDeEspecies)

        Assertions.assertEquals(listaDePatogenos[1].id, listaPatogenosRecuperados[1].id)
        Assertions.assertEquals(listaDePatogenos[1].tipo, listaPatogenosRecuperados[1].tipo)
        Assertions.assertEquals(listaDePatogenos[1].cantidadDeEspecies, listaPatogenosRecuperados[1].cantidadDeEspecies)
    }

    @AfterEach
    fun eliminarModelo() {
        dataDAO.clear()
    }



}