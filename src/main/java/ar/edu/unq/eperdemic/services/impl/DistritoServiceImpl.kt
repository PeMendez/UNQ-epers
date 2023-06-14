package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.modelo.exceptions.CoordenadasParaUnDistritoRepetidas
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeDistritoRepetido
import ar.edu.unq.eperdemic.persistencia.dao.DistritoDAO
import ar.edu.unq.eperdemic.services.DistritoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DistritoServiceImpl: DistritoService {


    @Autowired
    private lateinit var distritoDAO: DistritoDAO
    override fun crear(distrito: Distrito): Distrito {
        if (distritoDAO.recuperarPorNombre(distrito.nombre).isPresent) {
            throw NombreDeDistritoRepetido("Ya existe un distrito con el nombre dado")
        } else if (distritoDAO.existeDistritoEnCoordenadas(distrito.coordenadas)) {
            throw CoordenadasParaUnDistritoRepetidas("Ya existe un distrito con las coordenadas dadas")
        } else {
            return distritoDAO.save(distrito)
        }
    }

    override fun distritoMasEnfermo(): Distrito {
        TODO("Not yet implemented")
    }
}