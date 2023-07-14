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

    override fun agregarReporteDeInfeccion(idVectorInfectado: Long,
                                           capacidadDeBiomecanizacion: Int,
                                           capacidadDeContagio: Int,
                                           tipoDePatogeno: String,
                                           tipoDeVectorInfectado: TipoDeVector): InfeccionSegunPatogeno {
        val reporteSegunPatogeno = InfeccionSegunPatogeno(idVectorInfectado, capacidadDeBiomecanizacion, capacidadDeContagio, tipoDePatogeno, tipoDeVectorInfectado)
        infeccionSegunPatogenoDAO.save(reporteSegunPatogeno)
        return reporteSegunPatogeno
    }

    override fun findAllByVectorId(idVectorInfectado: Long): List<InfeccionSegunPatogeno> {
        return infeccionSegunPatogenoDAO.findAllByVectorId(idVectorInfectado)
    }
}