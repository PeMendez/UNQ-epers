package ar.edu.unq.eperdemic.utils.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.DataService
import java.sql.Connection

class DataServiceHibernate : DataService {

    val patogenoDAO = HibernatePatogenoDAO()
    val patogenoService = PatogenoServiceImpl(patogenoDAO)

    override fun crearSetDeDatosIniciales() {
        crearPatogenos()
        crearEspecies()
    }

    override fun eliminarTodo() {
        JDBCConnector.execute { conn: Connection ->
            conn.prepareStatement("TRUNCATE TABLE patogeno")
                .use { ps ->
                    ps.execute()
                }
        }
    }

    fun crearPatogenos() {
        var tiposDePatogenos = mutableListOf("virus", "bacteria", "bacteria")
        tiposDePatogenos.forEach {
                tipo -> patogenoService.crearPatogeno(Patogeno(tipo))
        }
    }

    fun crearEspecies() {

    }

}