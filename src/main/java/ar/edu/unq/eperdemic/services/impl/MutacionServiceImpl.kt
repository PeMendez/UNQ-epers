package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.MutacionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MutacionServiceImpl: MutacionService {

    @Autowired
    private lateinit var especieDAO: EspecieDAO
    @Autowired private lateinit var mutacionDAO: MutacionDAO


    override fun agregarMutacion(especieId: Long, mutacion: Mutacion): Mutacion {
        val especie = especieDAO.findByIdOrNull(especieId) ?: throw NoExisteElid("el id buscado no existe en la base de datos")
        mutacion.addEspecie(especie)
        mutacionDAO.save(mutacion)
        return mutacion
    }

    override fun recuperarMutacion(mutacionId: Long): Mutacion {
        return mutacionDAO.findByIdOrNull(mutacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
    }

    override fun recuperarTodas(): List<Mutacion> {
        return mutacionDAO.findAll().toList()
    }

    override fun recuperarTodas(page: Pageable): Page<Mutacion> {
        return mutacionDAO.findAll(page)
    }

}

