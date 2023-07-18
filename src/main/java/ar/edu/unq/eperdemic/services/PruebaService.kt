package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.PruebaCassandra

interface PruebaService {

    fun crearPrueba(pruebaCassandra: PruebaCassandra): PruebaCassandra
    fun eliminarTodo()
}