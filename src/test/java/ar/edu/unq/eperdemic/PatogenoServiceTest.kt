package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PatogenoServiceTest {
    private val dao: PatogenoDAO = JDBCPatogenoDAO()
    lateinit var patogeno: Patogeno
    lateinit var patogenoBacteria: Patogeno

    @BeforeEach
    fun crearModelo() {
        patogeno = Patogeno("Virus")
        patogenoBacteria = Patogeno("Bacteria")

    }

    @Test
    fun elPatogenoCreadoAhoraTieneUnIdAsignado(){
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
        // Modificamos los datos del patógeno
        patogeno.tipo = "Bacteria"
        patogeno.cantidadDeEspecies = 5000
        patogeno.id = 5

        // Actualizamos el patógeno en la base de datos
        dao.actualizar(patogeno)

        // Recuperamos el patógeno de la base de datos
        val patogenoRecuperado = dao.recuperar(patogeno.id!!)

        // Verificamos que los datos hayan sido actualizados correctamente
        Assertions.assertEquals(patogeno.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogeno.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)
    }

    @Test
    fun seRecuperanTodosLosPatogenosExistentes() {
        val listaDePatogenos : List<Patogeno> = listOf(patogenoBacteria, patogeno)
        dao.crear(patogeno)
        dao.crear(patogenoBacteria)

        Assertions.assertEquals(listaDePatogenos, dao.recuperarATodos())
    }

}