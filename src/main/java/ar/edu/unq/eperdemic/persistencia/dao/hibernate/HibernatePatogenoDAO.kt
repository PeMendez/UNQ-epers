package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernatePatogenoDAO : HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {

    override fun crear(patogeno: Patogeno): Patogeno {
        guardar(patogeno)

        return patogeno
    }

    override fun recuperarPatogeno(idDelPatogeno: Long): Patogeno {

        return recuperar(idDelPatogeno)
    }

    override fun recuperarATodos(): List<Patogeno> {
        val session = TransactionRunner.currentSession

        val hql = "from Patogeno"

        val query = session.createQuery(hql, Patogeno::class.java)

        return query.resultList
    }

    override fun especiesDePatogeno(patogenoId: Long ): List<Especie> {
        val session = TransactionRunner.currentSession

        val hql = """
                        from Especie e
                        where e.patogeno.id = :idBuscado 
            
        """

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("idBuscado", patogenoId)

        return query.resultList

    }


}