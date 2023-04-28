package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateVectorDAO : HibernateDAO<Vector>(Vector::class.java),VectorDAO {

    override fun enfermedades(vectorID: Long): List<Especie> {
        val session = TransactionRunner.currentSession
        val hql = """
                select e
                from Vector v
                inner join v.especies e
                where v.id = :vectorId
        """

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("vectorId", vectorID)

        return query.resultList
    }

    override fun crearVector(newVector: Vector): Vector {
        guardar(newVector)
        return newVector
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return recuperar(vectorId)
    }

    override fun recuperarTodos(): List<Vector> {
        val session = TransactionRunner.currentSession

        val hql = "from Vector "

        val query = session.createQuery(hql, Vector ::class.java)

        return query.resultList
    }
}