package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateVectorDAO : HibernateDAO<Vector>(Vector::class.java),VectorDAO {

    override fun infectar(vector: Vector, especie: Especie) {
        vector.especies.add(especie)
        guardar(vector)
    }

    override fun enfermedades(vectorID: Long): List<Especie> {
        return recuperarVector(vectorID).especies
    }

    override fun crearVector(tipo: TipoDeVector, ubicacion: Ubicacion): Vector {
        val newVector = Vector(tipo,ubicacion)
        guardar(newVector)
        return newVector
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return recuperar(vectorId)
    }

    override fun borrarVector(vectorId: Long) {
        val session = TransactionRunner.currentSession
        val hql = """
                delete from Vector v
                where v.id = :vectorABorrar
        """
        val query = session.createQuery(hql, Vector ::class.java)
        query.setParameter("vectorABorrar", vectorId)
        query.executeUpdate()
    }
}