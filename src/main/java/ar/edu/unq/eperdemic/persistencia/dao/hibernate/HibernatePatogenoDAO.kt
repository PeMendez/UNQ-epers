package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernatePatogenoDAO : HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {

    override fun crear(patogeno: Patogeno): Patogeno {
        guardar(patogeno)

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

    override fun especiesDePatogeno(patogenoId: Long ): List<Especie> {
        val session = TransactionRunner.currentSession

        val hql = """
                        from Especie e
                        where e.patogeno = :idBuscado 
            
        """

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("idBuscado", patogenoId)

        return query.resultList

    }

    /* fun agregarEspecie (id: Long, nombre: String, ubicacionId: Long): Especie{
       val session = TransactionRunner.currentSession

       val hql = """
                   from Ubicacion u
                   where u.id = :idBuscado
       """

       val query = session.createQuery(hql, Ubicacion::class.java)
       query.setParameter("idBuscado", ubicacionId)

       val ubicacion = query.singleResult
       val patogeno = recuperar(id)
       return patogeno.crearEspecie(nombre, ubicacion.nombre)
   }*/
}