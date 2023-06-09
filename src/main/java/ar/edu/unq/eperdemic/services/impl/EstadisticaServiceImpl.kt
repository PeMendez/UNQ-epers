package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElNombreDeLaUbicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.EstadisticaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/*@Service
@Transactional
class EstadisticaServiceImpl : EstadisticaService {

    @Autowired
    private lateinit var ubicacionDAO: UbicacionDAO
    @Autowired private lateinit var especie: EspecieServiceImpl


    override fun especieLider(): Especie {
        return especie.especieLider()
    }

    override fun lideres(): List<Especie> {
        return especie.lideres()
    }

    override fun reporteDeContagios(nombreDeLaUbicacion: String): ReporteDeContagios {
        val ubicacion = try {
            ubicacionDAO.recuperarUbicacionPorNombre(nombreDeLaUbicacion)
        }catch (e:EmptyResultDataAccessException){
            throw NoExisteElNombreDeLaUbicacion("No se encontró una ubicación con el nombre dado: $nombreDeLaUbicacion")
        }
        val cantidadVectores = ubicacionDAO.recuperarVectores(ubicacion.id!!).size
        val cantidadInfectados = ubicacionDAO.recuperarVectores(ubicacion.id!!).filter {v -> !v.estaSano()}.size
        val especieLider = especie.especieLiderDeUbicacion(ubicacion.id!!)
        val reporte = ReporteDeContagios(cantidadVectores, cantidadInfectados, especieLider.nombre)
        return reporte
    }


}*/