package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class VectorServiceImpl(): VectorService {

    @Autowired
    private lateinit var especieDAO: EspecieDAO

    @Autowired
    private lateinit var vectorDAO: VectorDAO
    @Autowired
    private lateinit var ubicacionDAO: UbicacionDAO

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        vectorDAO.findByIdOrNull(vectorInfectado.id)?: throw NoExisteElid("No existe el ID del vector")
        vectores.forEach { v ->
            vectorInfectado.intentarInfectarConEspecies(v)
            vectorDAO.save(v)
        }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        vectorDAO.findByIdOrNull(vector.id)?: throw NoExisteElid("No existe el ID del vector")
        especieDAO.findByIdOrNull(especie.id!!)?: throw NoExisteElid("El id de Especie ingresado no existe")

        especie.let { e ->  vector.serInfectadoCon(e)}
        vector.let { v -> vectorDAO.save(v) }
    }

    override fun enfermedades(vectorId: Long): List<Especie> {
        vectorDAO.findByIdOrNull(vectorId)?: throw NoExisteElid("No existe el ID del vector")
        return vectorDAO.findEnfermedades(vectorId)
    }

    override fun crearVector(tipo: TipoDeVector, ubicacionId: Long): Vector {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("No existe el ID de la ubicación")
        val nuevoVector = Vector(tipo,ubicacion)
        return vectorDAO.save(nuevoVector)
    }

    override fun recuperarVector(vectorId: Long): Vector {
        return vectorDAO.findByIdOrNull(vectorId)?: throw NoExisteElid("No existe el ID del vector")
    }

    override fun borrarVector(vectorId: Long) {
        val vectorABorrar = recuperarVector(vectorId)
        return vectorDAO.delete(vectorABorrar)
    }

    override fun recuperarTodos(): List<Vector> {
        return vectorDAO.findAll()
    }

    override fun vectoresEnUbicacionID(ubicacionId: Long): List<Vector> {
        return vectorDAO.findAllByUbicacionId(ubicacionId)
    }
}

