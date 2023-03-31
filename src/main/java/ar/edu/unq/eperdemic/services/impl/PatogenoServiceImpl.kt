package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService

class PatogenoServiceImpl(var patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return patogenoDAO.crear(patogeno)
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return patogenoDAO.recuperar(id)
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return patogenoDAO.recuperarATodos()
    }

    override fun agregarEspecie(id: Long, nombre: String, paisDeOrigen: String): Especie {
        val patogeno = patogenoDAO.recuperar(id)
        val especie = patogeno.crearEspecie(nombre, paisDeOrigen)

        actualizarPatogeno(patogeno)
        return especie
    }

    override fun actualizarPatogeno(patogeno: Patogeno) {
        patogenoDAO.actualizar(patogeno)
    }

}