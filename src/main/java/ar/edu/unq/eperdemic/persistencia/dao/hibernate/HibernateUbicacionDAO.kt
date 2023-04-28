package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import javax.persistence.NoResultException

open class HibernateUbicacionDAO : HibernateDAO<Ubicacion>(Ubicacion::class.java), UbicacionDAO {

    override fun crearUbicacion(ubicacion: Ubicacion): Ubicacion {
        guardar(ubicacion)
        return ubicacion
    }

    override fun recuperarPorNombre(nombreUbicacion: String): Ubicacion {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Ubicacion u
                    where u.nombre = :nombreBuscado
        """

        val query = session.createQuery(hql, Ubicacion::class.java)
        query.setParameter("nombreBuscado", nombreUbicacion)
        return query.singleResult
    }

    override fun recuperarTodos(): List<Ubicacion> {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Ubicacion
        """

        val query = session.createQuery(hql, Ubicacion::class.java)

        return query.resultList

    }

    override fun recuperarVectores(ubicacionId: Long) : List<Vector> {
        val session = TransactionRunner.currentSession

        val hql = """
                    select v 
                    from Vector v 
                    where v.ubicacion.id = :idBuscado
        """

        val query = session.createQuery(hql, Vector::class.java)
        query.setParameter("idBuscado", ubicacionId)

        return query.resultList
    }

    override fun recuperarUbicacionPorNombre(nombreUbicacion: String) : Ubicacion{
        val session = TransactionRunner.currentSession
        val hql = """ 
                    from Ubicacion u 
                    where u.nombre = :nombreBuscado
        """
        val query = session.createQuery(hql, Ubicacion::class.java)
        query.setParameter("nombreBuscado", nombreUbicacion)
        return query.singleResult
    }


}