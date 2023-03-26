package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
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
        //guardo el patógeno
        patogenoDAO.crear(patogeno)

        //me guardo el id del patógeno
        idDelPatogeno=patogeno.id!!.toLong()

        //borro la tabla de patógenos
        dataDAO.clear()

        //reviso si se borró
        assertSame("El id del patogeno $patogeno no existe",patogenoDAO.actualizar(patogeno))
    }
}