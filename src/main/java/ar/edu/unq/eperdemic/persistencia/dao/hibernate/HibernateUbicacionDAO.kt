package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import javax.persistence.NoResultException

open class HibernateUbicacionDAO : HibernateDAO<Ubicacion>(Ubicacion::class.java), UbicacionDAO {

    val vectorDAO = HibernateVectorDAO()

    override fun mover(vectorId: Long, ubicacionid: Long) {
        val ubicacion = recuperar(ubicacionid)
        val vector = vectorDAO.recuperarVector(vectorId)

        vector.mover(ubicacion)

        vectorDAO.actualizar(vector)

    }

    override fun expandir(ubicacionId: Long) {
        TODO("Not yet implemented")
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Ubicacion u
                    where u.nombre = :nombreBuscado
        """

        val query = session.createQuery(hql, Ubicacion::class.java)
        query.setParameter("nombreBuscado", nombreUbicacion)

        try {
            query.singleResult
        } catch (e: NoResultException) {
            val ubicacionCreada = Ubicacion(nombreUbicacion)
            guardar(ubicacionCreada)
            return ubicacionCreada
        }
        throw Exception("Ya existe una ubicacion con ese nombre.")
    }

    override fun recuperarTodos(): List<Ubicacion> {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Ubicacion
        """

        val query = session.createQuery(hql, Ubicacion::class.java)

        return query.resultList

    }

    override fun recuperar(ubicacionId: Long): Ubicacion {
        val session = TransactionRunner.currentSession

        val hql = """
                    from Ubicacion u
                    where u.id = :idBuscado
        """

        val query = session.createQuery(hql, Ubicacion::class.java)
        query.setParameter("idBuscado", ubicacionId)

        return query.singleResult
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
}