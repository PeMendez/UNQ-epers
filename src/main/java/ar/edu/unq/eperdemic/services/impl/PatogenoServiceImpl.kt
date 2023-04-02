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

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        TODO("not implemented")
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        TODO("Not yet implemented")
    }

    override fun esPandemia(especieId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun recuperarEspecie(id: Long): Especie {
        TODO("Not yet implemented")
    }

}