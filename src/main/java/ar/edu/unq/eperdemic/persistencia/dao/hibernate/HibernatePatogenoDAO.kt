package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernatePatogenoDAO : HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {

    override fun crear(patogeno: Patogeno): Patogeno {
        guardar(patogeno)
        val session = TransactionRunner.currentSession

        val hql = """
                    select LAST_INSERT_ID()
                    from Patogeno 
        """

        val queryId = session.createQuery(hql, Patogeno::class.java)

        patogeno.id = queryId.singleResult as Long

        return patogeno
    }

    override fun recuperar(idDelPatogeno: Long): Patogeno {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Patogeno p
                    where p.id = :idBuscado
        """

        val query = session.createQuery(hql, Patogeno::class.java)
        query.setParameter("idBuscado", idDelPatogeno)

        return query.singleResult
    }

    override fun recuperarATodos(): List<Patogeno> {
        val session = TransactionRunner.currentSession

        val hql = "from Patogeno"

        val query = session.createQuery(hql, Patogeno::class.java)

        return query.resultList
    }

}