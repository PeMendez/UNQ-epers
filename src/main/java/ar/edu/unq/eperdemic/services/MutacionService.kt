package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Mutacion
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MutacionService {
  fun agregarMutacion(especieId: Long, mutacion: Mutacion): Mutacion
  fun recuperarMutacion(mutacionId: Long): Mutacion
  fun recuperarTodas(): List<Mutacion>
  fun recuperarTodas(page: Pageable): Page<Mutacion>
}

