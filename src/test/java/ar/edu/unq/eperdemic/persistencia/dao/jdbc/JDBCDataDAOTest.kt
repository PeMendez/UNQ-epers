package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class JDBCDataDAOTest {

    private var dataDAO: DataDAO=JDBCDataDAO()
    private var patogenoDAO: PatogenoDAO = JDBCPatogenoDAO()
    lateinit var patogeno: Patogeno
    private var idDelPatogeno: Long = 0

    @BeforeEach
    fun setUp() {
        patogeno = Patogeno("Virus")
    }

    @Test
    fun clear() {
        // se crea y persiste el patogeno
        patogenoDAO.crear(patogeno)

        // se obtiene la lista de todos los patogenos
        var patogenosRecuperados : List<Patogeno> = patogenoDAO.recuperarATodos()

        // se borran los datos persistidos
        dataDAO.clear()

        // se obtiene una lista de todos los patogenos luego de borrar los datos persistidos
        var patogenosRecuperadosPostClear : List<Patogeno> = patogenoDAO.recuperarATodos()

        Assertions.assertTrue(patogenosRecuperados.isNotEmpty())
        Assertions.assertTrue(patogenosRecuperadosPostClear.isEmpty())
    }
}