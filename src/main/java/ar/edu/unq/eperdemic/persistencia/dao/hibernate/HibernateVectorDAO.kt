package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


class HibernateVectorDAO: VectorDAO {
    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        vectores.forEach{v ->
            vectorInfectado.especies.forEach { e ->
                if (vectorInfectado.esContagioExitoso(v,e)) {
                    infectar(v, e)
                }
            }
        }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        vector.infectar(especie)
        //aca va la consulta hql

    }

    override fun enfermedades(vectorID: Long): List<Especie> {
        val session = TransactionRunner.currentSession
        val hql = """ 
                    from Vector v
                    inner join v.especies e
                    where v.id = :vectorId 
                """
        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("vectorId", vectorID)
        return query.resultList
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun recuperarVector(vectorId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun borrarVector(vectorId: Long) {
        TODO("Not yet implemented")
    }
}