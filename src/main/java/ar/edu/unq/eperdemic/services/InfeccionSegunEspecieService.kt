package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.InfeccionSegunEspecie
import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector

interface InfeccionSegunEspecieService {

    fun agregarReporteDeInfeccion(idVectorInfectado: Long,nombreEspecie: String, paisOrigenEspecie: String, tipoDeVectorInfectado: TipoDeVector): InfeccionSegunEspecie

    fun findAll(): List<InfeccionSegunEspecie>
}