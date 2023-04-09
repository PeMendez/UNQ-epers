package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class VectorServiceImpl(private val vectorDAO: VectorDAO): VectorService {

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        return TransactionRunner.runTrx {vectorDAO.contagiar(vectorInfectado,vectores) }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        TODO("Not yet implemented")
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        TODO("Not yet implemented")
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun recuperarVector(vectorId: Long): Vector {
        TODO("Not yet implemented")
    }

    override fun borrarVector(vectorId: Long) {
        TODO("Not yet implemented")
    }
}