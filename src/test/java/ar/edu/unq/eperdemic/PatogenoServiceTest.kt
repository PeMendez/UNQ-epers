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
    }
}