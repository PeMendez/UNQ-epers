package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService

class PatogenoServiceImpl(var patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        TODO("not implemented")
    }

    override fun agregarEspecie(id: Long, nombre: String, paisDeOrigen: String): Especie {
        TODO("not implemented")
    }

}