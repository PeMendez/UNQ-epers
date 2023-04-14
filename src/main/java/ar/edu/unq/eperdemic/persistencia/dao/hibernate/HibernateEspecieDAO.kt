package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernateEspecieDAO : HibernateDAO<Especie>(Especie::class.java), EspecieDAO {
    override fun recuperarEspecie(id: Long): Especie {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Especie e
                    where e.id = :idBuscado
        """

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("idBuscado", id)

        return query.singleResult
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        val session = TransactionRunner.currentSession
        val hql = """
                select count(*) from Vector v
                where v.especie.id = :especieId and v.estaInfectado
        """
        val query = session.createQuery(hql)
        query.setParameter("especieId", especieId)
        return query.uniqueResult() as Int
    }


    override fun recuperarTodas(): List<Especie> {
        val session = TransactionRunner.currentSession
        val hql = """
                from Especie e
            """
        val query = session.createQuery(hql, Especie::class.java)
        return query.resultList
    }


}