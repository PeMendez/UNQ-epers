package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.persistencia.dao.DataDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection

class JDBCDataDAO : DataDAO {
    override fun clear(){
        execute { conn: Connection ->
            conn.prepareStatement("DROP TABLE patogeno")
                .use { ps ->
                    ps.execute()
                }
        }
    }
}

