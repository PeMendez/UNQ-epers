package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Patogeno
import java.sql.Connection
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute

interface PatogenoDAO {
    fun crear(patogeno: Patogeno): Patogeno
    fun recuperar(idDelPatogeno: Long): Patogeno
    fun recuperarATodos() : List<Patogeno>
}