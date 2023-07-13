package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import org.springframework.stereotype.Service

interface InfeccionSegunPatogenoService {

    fun agregarReporteDeInfeccion(capacidadDeBiomecanizacion: Int,
                                  capacidadDeContagio: Int,
                                  tipoDePatogeno: String,
                                  tipoDeVectorInfectado: TipoDeVector,
                                  idDeVectorEnfermo: Long? = null): InfeccionSegunPatogeno
}