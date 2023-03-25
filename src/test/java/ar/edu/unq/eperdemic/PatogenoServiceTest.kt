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
    @BeforeEach
    fun crearModelo() {
        patogeno = Patogeno("Virus3")
    }
    @Test
    fun seCreaUnPatogenoEnLaBaseDeDatos() {
        dao.crear(patogeno)
        var patogenoRecuperado :Patogeno = dao.recuperar(patogeno.id!!.toLong())

        //un vez recueprado se crea un objeto de caracteristicas iguales
        Assertions.assertEquals(patogeno.id, patogenoRecuperado.id)
        Assertions.assertEquals(patogeno.tipo, patogenoRecuperado.tipo)
        Assertions.assertEquals(patogeno.cantidadDeEspecies, patogenoRecuperado.cantidadDeEspecies)

        //pero no es el mismo objeto
        Assertions.assertFalse(patogeno===patogenoRecuperado)
    }
}