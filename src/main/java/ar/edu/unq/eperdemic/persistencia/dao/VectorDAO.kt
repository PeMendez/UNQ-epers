package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector

interface VectorDAO {
    fun enfermedades(vectorID: Long) : List<Especie>
    fun crearVector(tipo: TipoDeVector, ubicacion: Ubicacion): Vector
    fun recuperarVector(vectorId: Long): Vector
    fun borrarVector(vectorId: Long)
    fun recuperarTodos() : List<Vector>
    fun guardar(entity : Vector)

}