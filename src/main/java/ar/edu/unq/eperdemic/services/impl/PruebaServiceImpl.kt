package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.PruebaCassandra
import ar.edu.unq.eperdemic.persistencia.dao.PruebaDAO
import ar.edu.unq.eperdemic.services.PruebaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PruebaServiceImpl: PruebaService {

    @Autowired
    private lateinit var pruebaDAO: PruebaDAO

    override fun crearPrueba(pruebaCassandra: PruebaCassandra): PruebaCassandra {
        val cassandra = pruebaDAO.save(pruebaCassandra)
        return cassandra
    }

    override fun eliminarTodo() {
        pruebaDAO.deleteAll()
    }
}