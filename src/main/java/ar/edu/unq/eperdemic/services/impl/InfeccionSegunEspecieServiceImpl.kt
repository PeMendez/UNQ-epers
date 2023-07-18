package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.InfeccionSegunEspecie
import ar.edu.unq.eperdemic.modelo.InfeccionSegunPatogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.InfeccionSegunEspecieDAO
import ar.edu.unq.eperdemic.services.InfeccionSegunEspecieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class InfeccionSegunEspecieServiceImpl: InfeccionSegunEspecieService {

    @Autowired
    private lateinit var infeccionSegunEspecieDAO: InfeccionSegunEspecieDAO

    override fun agregarReporteDeInfeccion(idVectorInfectado: Long,
                                           nombreEspecie: String,
                                           paisOrigenEspecie: String,
                                           tipoDeVectorInfectado: TipoDeVector): InfeccionSegunEspecie {
        val reporte = InfeccionSegunEspecie(idVectorInfectado, nombreEspecie, paisOrigenEspecie, tipoDeVectorInfectado)
        return infeccionSegunEspecieDAO.save(reporte)
    }

    override fun findAll(): List<InfeccionSegunEspecie> {
        return infeccionSegunEspecieDAO.findAll()
    }
}