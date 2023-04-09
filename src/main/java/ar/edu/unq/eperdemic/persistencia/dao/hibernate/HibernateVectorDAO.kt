package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO

class HibernateVectorDAO: VectorDAO {
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
        //aca va la consulta hql

    }
}