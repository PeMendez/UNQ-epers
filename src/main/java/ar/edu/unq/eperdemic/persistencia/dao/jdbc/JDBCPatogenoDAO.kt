package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection
import java.sql.Statement

class JDBCPatogenoDAO : PatogenoDAO {


    override fun crear(patogeno: Patogeno): Patogeno {
        val insertQuery = "INSERT INTO patogeno (tipo, cantidadDeEspecies) VALUES (?, ?)"
        var id: Long = 0
        execute { conn: Connection ->
            conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS).use { ps ->
                ps.setString(1, patogeno.tipo)
                ps.setInt(2, patogeno.cantidadDeEspecies)
                ps.executeUpdate()
                val resultSet = ps.generatedKeys
                while (resultSet.next()) {
                    id = resultSet.getLong(1)
                }
            }
            patogeno.id = id
        }
        return patogeno
    }

//   override fun actualizar(patogeno: Patogeno) {
//        execute { conn ->
//            val updateQuery = "UPDATE patogeno SET tipo = ?, cantidadDeEspecies = ? WHERE id = ?"
//            val ps = conn.prepareStatement(updateQuery)
//            ps.setString(1, patogeno.tipo)
//            ps.setInt(2, patogeno.cantidadDeEspecies)
//            patogeno.id?.let { ps.setLong(3, it) }
//            if (ps.executeUpdate() != 1) {
//                throw RuntimeException("El id del patogeno $patogeno no existe")
//            }
//            ps.close()
//        }
//    }

    override fun recuperar(patogenoId: Long): Patogeno {
        return execute { conn: Connection ->
            conn.prepareStatement("SELECT id, tipo, cantidadDeEspecies FROM patogeno WHERE id = ?")
                .use { ps ->
                    ps.setLong(1, patogenoId)
                    val resultSet = ps.executeQuery()
                    var patogeno: Patogeno? = null
                    while (resultSet.next()) {
                        if (patogeno != null) {
                            throw RuntimeException("Existe más de un patogeno con el id $patogenoId")
                        }
                        patogeno = Patogeno(resultSet.getString("tipo") )
                        patogeno.id = resultSet.getLong("id")
                        patogeno.cantidadDeEspecies = resultSet.getInt("cantidadDeEspecies")
                    }
                    patogeno!!
                }
        }
    }

    override fun recuperarATodos(): List<Patogeno> {
        val patogenos = mutableListOf<Patogeno>()

        return execute { conn: Connection ->
            conn.prepareStatement("SELECT * FROM patogeno ORDER BY tipo ASC")
                .use { ps ->
                    val resultSet = ps.executeQuery()
                    while (resultSet.next()){
                        val patogeno = Patogeno(resultSet.getString("tipo"))
                        patogeno.id = resultSet.getLong("id")
                        patogeno.cantidadDeEspecies = resultSet.getInt("cantidadDeEspecies")
                        patogenos.add(patogeno)
                    }
                }
            patogenos
        }
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