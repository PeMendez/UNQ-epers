package ar.edu.unq.eperdemic.utils.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.DataService
import java.sql.Connection

class DataServiceHibernate : DataService {

    val patogenoDAO = HibernatePatogenoDAO()
    val patogenoService = PatogenoServiceImpl(patogenoDAO)

    override fun crearSetDeDatosIniciales() {

    }

    override fun eliminarTodo() {

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