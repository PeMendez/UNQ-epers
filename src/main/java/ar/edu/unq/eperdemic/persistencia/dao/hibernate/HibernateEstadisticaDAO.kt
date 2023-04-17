package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.EstadisticaDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEstadisticaDAO : EstadisticaDAO {


    override fun especieLider(): Especie {
        val session = TransactionRunner.currentSession
        val hql = """ e
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
        val hql = """ e 
                from Vector v
                join v.especies e
        """
        val query = session.createQuery(hql, Especie::class.java)
        return query.resultList
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        return ReporteDeContagios(1,1,"a")
    }


}