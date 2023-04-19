package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
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
        val hql =  """
                    select count(e.id)
                    from Vector v
                    join v.especies e
                    where e.id = :especieId
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

    override fun especieLider(): Especie {
        val session = TransactionRunner.currentSession
        val hql = """select e
                    from Vector v
                    join v.especies e
                    where v.tipo in (${TipoDeVector.Persona.ordinal})
                    group by e 
                    order by count(v) desc
        """
        val query = session.createQuery(hql, Especie::class.java)
        return query.resultList.first()

    }


    override fun lideres(): List<Especie> {
        val session = TransactionRunner.currentSession
        val hql = """ select e 
                from Vector v
                join v.especies e
                where v.tipo in (${TipoDeVector.Persona.ordinal}, ${TipoDeVector.Animal.ordinal})
                group by e 
                order by count(v) desc
        """
        val query = session.createQuery(hql, Especie::class.java)
        return query.setMaxResults(10).resultList
    }


}