package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector

interface VectorDAO {
    fun contagiar(vectorInfectado: Vector,vectores: List<Vector>)
    fun infectar(vector: Vector, especie: Especie)
}