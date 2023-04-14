package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateVectorDAO : HibernateDAO<Vector>(Vector::class.java),VectorDAO {
    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {

        vectores.forEach{v ->
            vectorInfectado.especies.forEach { e ->
                if (vectorInfectado.esContagioExitoso(v,e)) {
                    infectar(v, e)
                }
            }
        }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        vector.infectar(especie)
        guardar(vector)
    }

    override fun enfermedades(vectorID: Long): List<Especie> {

        return recuperarVector(vectorID).especies

        //val session = TransactionRunner.currentSession
        //val hql = """
        //        from Vector v
        //        inner join v.especies e
        //        where v.id = :vectorId
        //        """
        //val query = session.createQuery(hql, Especie::class.java)
        //query.setParameter("vectorId", vectorID)
        //return query.resultList
    }

    override fun crearVector(tipo: TipoDeVector, ubicacion: Ubicacion): Vector {
        val newVector = Vector(null,tipo,ubicacion)
        guardar(newVector)
        return newVector
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return recuperar(vectorId)
    }

    override fun borrarVector(vectorId: Long) {
        TODO("Not yet implemented")
    }
}