package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection
import java.sql.Statement

class JDBCPatogenoDAO : PatogenoDAO {



    override fun crear(patogeno: Patogeno) : Patogeno{
        execute { conn: Connection ->
            var id: Long = 0
            conn.prepareStatement("INSERT INTO patogeno (tipo, cantidadDeEspecies) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)
                .use  { ps ->
                    ps.setString(1,patogeno.tipo)
                    ps.setInt(2, patogeno.cantidadDeEspecies)
                    ps.execute()
                    val ids = ps.generatedKeys
                    if (ids.next()) {
                        id = ids.getLong(1)
                    }
                    ps.close()
                    patogeno.id=id
                }
        }
        return patogeno
    }

    override fun actualizar(patogeno: Patogeno) {
        TODO("not implemented")
    }

    override fun recuperar(patogenoId: Long): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarATodos(): List<Patogeno> {
        TODO("not implemented")
    }

    init {
        val initializeScript = javaClass.classLoader.getResource("createAll.sql").readText()
        execute {connection ->
            val scripts = initializeScript.split(";")
            scripts.forEach{ script ->
                connection.prepareStatement(script)
                    .use {
                        it.execute()
                    }
            }
            null
        }
    }

}