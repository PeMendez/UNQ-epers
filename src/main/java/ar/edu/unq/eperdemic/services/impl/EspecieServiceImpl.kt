package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteUnaEspecieLider
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
@Transactional
class EspecieServiceImpl() : EspecieService {

    @Autowired
    private lateinit var especieDAO: EspecieDAO
    override fun recuperarEspecie(id: Long): Especie {
        return especieDAO.findByIdOrNull(id)?: throw NoExisteElid("el id buscado no existe en la base de datos")
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        TODO("Not yet implemented")
    }

    override fun recuperarTodas(): List<Especie> {
        TODO("Not yet implemented")
    }

    override fun especieLider(): Especie {
        TODO("Not yet implemented")
    }

    override fun lideres(): List<Especie> {
        TODO("Not yet implemented")
    }
    /*override fun recuperarEspecie(id: Long): Especie {
        return runTrx { hibernateEspecieDAO.recuperar(id)?: throw NoExisteElid("el id buscado no existe en la base de datos") }
    }

    override fun cantidadDeInfectados(especieId: Long ): Int {
        runTrx { hibernateEspecieDAO.recuperar(especieId)?: throw NoExisteElid("el id buscado no existe en la base de datos") }
        return runTrx { hibernateEspecieDAO.cantidadDeInfectados(especieId) }
    }

    override fun recuperarTodas(): List<Especie>{
        return runTrx { hibernateEspecieDAO.recuperarTodas() }
    }


    override fun especieLider(): Especie {
        return runTrx {
            try {
                hibernateEspecieDAO.especieLider()
            } catch (e: NoSuchElementException) {
                throw NoExisteUnaEspecieLider("No hay una especie lider actualmente.")
            }
        }
    }


    override fun lideres(): List<Especie> {
        return runTrx { hibernateEspecieDAO.lideres() }
    }*/

}