package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.InfeccionSegunPatogenoDAO
import ar.edu.unq.eperdemic.services.InfeccionSegunPatogenoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class InfeccionSegunPatogenoServiceImpl: InfeccionSegunPatogenoService {

    @Autowired
    private lateinit var infeccionSegunPatogenoDAO : InfeccionSegunPatogenoDAO

    override fun agregarReporteDeInfeccion(capacidadDeBiomecanizacion: Int,
                                           capacidadDeContagio: Int,
                                           tipoDePatogeno: String,
                                           tipoDeVectorInfectado: TipoDeVector,
                                           idDeVectorEnfermo: Long?): InfeccionSegunPatogeno {
        val reporteSegunPatogeno = InfeccionSegunPatogeno(capacidadDeBiomecanizacion, capacidadDeContagio, tipoDePatogeno, tipoDeVectorInfectado, idDeVectorEnfermo)
        infeccionSegunPatogenoDAO.save(reporteSegunPatogeno)
        return reporteSegunPatogeno
    }
}