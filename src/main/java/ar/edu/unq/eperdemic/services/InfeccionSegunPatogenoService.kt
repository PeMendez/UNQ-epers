package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.InfeccionReporte
import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import org.springframework.stereotype.Service

interface InfeccionSegunPatogenoService {

    fun agregarReporteDeInfeccion(idVectorInfectado: Long,
                                  capacidadDeBiomecanizacion: Int,
                                  capacidadDeContagio: Int,
                                  tipoDePatogeno: String,
                                  tipoDeVectorInfectado: TipoDeVector): InfeccionSegunPatogeno

    fun findAllByVectorId(idVectorInfectado: Long): List<InfeccionSegunPatogeno>
}