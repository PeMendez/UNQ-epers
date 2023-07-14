package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.InfeccionReporte
import ar.edu.unq.eperdemic.persistencia.dao.InfeccionReporteDAO
import ar.edu.unq.eperdemic.services.InfeccionReporteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class InfeccionReporteServiceImpl : InfeccionReporteService {

    @Autowired
    private lateinit var infeccionReporteDAO : InfeccionReporteDAO

    override fun agregarInfeccionReporte(idVectorInfectado: Long, idEspecie: Long): InfeccionReporte {
        val reporte = InfeccionReporte(idVectorInfectado, idEspecie)
        return infeccionReporteDAO.save(reporte)
    }

    override fun findAllByVectorId(idVectorInfectado: Long): List<InfeccionReporte> {
        return infeccionReporteDAO.findAllByVectorId(idVectorInfectado)
    }
}