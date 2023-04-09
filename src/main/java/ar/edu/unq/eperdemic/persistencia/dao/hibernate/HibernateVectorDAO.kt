package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateVectorDAO: VectorDAO {
    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {


    }

    override fun infectar(vector: Vector, especie: Especie) {
        TODO("Not yet implemented")
    }
}