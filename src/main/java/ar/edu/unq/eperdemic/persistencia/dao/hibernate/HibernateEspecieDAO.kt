package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernateEspecieDAO : HibernateDAO<Especie>(Especie::class.java), EspecieDAO {

    override fun recuperarEspecie(id: Long): Especie {
        return recuperar(id)
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
        val result = query.uniqueResult() as? Long
        return result!!.toInt()
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

    override fun especieLiderDeUbicacion(ubicacionId: Long) : Especie {
        val session = TransactionRunner.currentSession
        val hql = """select e
                    from Vector v
                    join v.especies e
                    where v.ubicacion.id = :ubicacionId
                    group by e 
                    order by count(v) desc
        """
        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("ubicacionId", ubicacionId)
        return query.resultList.first()
    }


}