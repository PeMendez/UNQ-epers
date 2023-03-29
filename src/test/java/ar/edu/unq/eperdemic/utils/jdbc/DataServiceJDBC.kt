package ar.edu.unq.eperdemic.utils.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.utils.DataService
import java.sql.Connection

class DataServiceJDBC : DataService {

    val dao : PatogenoDAO = JDBCPatogenoDAO()

    override fun crearSetDeDatosIniciales() {
        var tiposDePatogenos = mutableListOf("virus", "patogeno", "bacteria")
        tiposDePatogenos.forEach {
                tipo -> dao.crear(Patogeno(tipo))
        }
    }

    override fun eliminarTodo() {
        JDBCConnector.execute { conn: Connection ->
            conn.prepareStatement("TRUNCATE TABLE patogeno")
                .use { ps ->
                    ps.execute()
                }
        }
    }

}