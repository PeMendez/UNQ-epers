package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Mutacion

interface MutacionService {
  fun agregarMutacion(especieId: Long, mutacion: Mutacion): Mutacion
}