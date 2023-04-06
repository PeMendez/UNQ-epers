package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.services.EspecieService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner.runTrx

class EspecieServiceImpl(val hibernateDAO: HibernateEspecieDAO) : EspecieService {

    override fun recuperarEspecie(id: Long): Especie {
        return runTrx { hibernateDAO.recuperar(id)}
    }

    override fun cantidadDeInfectados(especieId: Long ): Int{
        TODO("not implemented")
    }

    override fun recuperarTodas(): List<Especie>{
        TODO("not implemented")
    }


}