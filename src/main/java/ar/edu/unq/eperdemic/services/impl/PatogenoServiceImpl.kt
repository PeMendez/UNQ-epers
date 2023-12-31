package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NingunVectorAInfectarEnLaUbicacionDada
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeEspecieRepetido
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PatogenoServiceImpl : PatogenoService {

    @Autowired private lateinit var patogenoDAO: PatogenoDAO
    private val diosito = Random
    @Autowired private lateinit var ubicacionDAO: UbicacionDAO
    @Autowired private lateinit var vectorDAO: VectorDAO
    @Autowired private lateinit var vectorService: VectorServiceImpl
    @Autowired private lateinit var especieDAO: EspecieDAO


    override fun crearPatogeno(patogeno: Patogeno): Patogeno {
        return  patogenoDAO.save(patogeno)
    }

    override fun recuperarPatogeno(id: Long): Patogeno {
        return patogenoDAO.findByIdOrNull(id)?: throw NoExisteElid("el id buscado no existe en la base de datos") }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        return patogenoDAO.findAll().toList()
    }

    override fun recuperarATodosLosPatogenos(page: Pageable): Page<Patogeno>{
        return patogenoDAO.findAll(page)
    }

    override fun agregarEspecie(id: Long, nombre: String, ubicacionId: Long): Especie {
        if (especieDAO.existeElNombreEnLaBase(nombre)) {
            throw NombreDeEspecieRepetido("Ya existe una especie con el nombre $nombre")
        }
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("No existe la ubicacion")
        val patogeno = patogenoDAO.findByIdOrNull(id)?: throw NoExisteElid("No existe el patógeno")
        val vectores = vectorDAO.findAllByUbicacionId(ubicacionId)
        val vectorAInfectar = try{
            vectores[diosito.decidir(vectores.size)-1]
        } catch (e: Exception){
            throw NingunVectorAInfectarEnLaUbicacionDada("No hay ningún vector en la ubicación dada")
        }
        val especie = patogeno.crearEspecie(nombre, ubicacion.nombre)
        especieDAO.save(especie)
        vectorService.infectar(vectorAInfectar, especie)
        return especie
    }

    override fun esPandemia(especieId: Long): Boolean {
        return patogenoDAO.esPandemia(especieId)
    }

    override fun especiesDePatogeno(patogenoId: Long): List<Especie> {
        return patogenoDAO.findAllEspeciesById(patogenoId)
    }


}

