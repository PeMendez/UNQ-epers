package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteUnaEspecieLider
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.EspecieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EspecieServiceImpl() : EspecieService {

    @Autowired
    private lateinit var especieDAO: EspecieDAO



    override fun recuperarEspecie(id: Long): Especie {
        return especieDAO.findByIdOrNull(id) ?: throw NoExisteElid("el id buscado no existe en la base de datos")
    }

    override fun recuperarTodas(): List<Especie> {
        return especieDAO.findAll().toList()
    }

    override fun lideres(): List<Especie> {
        return especieDAO.lideres().take(10)
    }

    override fun cantidadDeInfectados(especieId: Long): Int {
        this.recuperarEspecie(especieId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
        return especieDAO.cantidadDeInfectados(especieId)

    }

    override fun especieLider(): Especie {
        try{
            return especieDAO.especieLider().first()
        }catch (e:NoSuchElementException) {
            throw NoExisteUnaEspecieLider("No hay una especie lider actualmente.")
        }
    }

    override fun especieLiderDeUbicacion(ubicacionId: Long) : Especie {
        return especieDAO.especieLiderDeUbicacion(ubicacionId).first()
    }

}

