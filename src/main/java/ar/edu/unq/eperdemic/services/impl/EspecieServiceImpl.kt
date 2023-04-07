package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EspecieServiceImpl(val hibernateEspecieDAO: HibernateEspecieDAO) : EspecieService {

    override fun recuperarEspecie(id: Long): Especie {
        return runTrx { hibernateEspecieDAO.recuperar(id)}
    }

    override fun cantidadDeInfectados(especieId: Long ): Int{
        return runTrx { hibernateEspecieDAO.cantidadDeInfectados(especieId) }
    }

    override fun recuperarTodas(): List<Especie>{
        return runTrx { hibernateEspecieDAO.recuperarTodas() }
    }


}