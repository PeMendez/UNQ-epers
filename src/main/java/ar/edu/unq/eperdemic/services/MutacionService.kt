package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Mutacion

interface MutacionService {
    fun mutar (especieId: Long, mutacionId: Long)
    /* Operaciones CRUD */
    fun crearMutacion(mutacion: Mutacion): Mutacion
    fun recuperarMutacion(mutacionId: Long): Mutacion
}