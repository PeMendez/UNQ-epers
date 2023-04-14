package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.*
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.VectorService

import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class VectorServiceImplTest {

    val hibernate = HibernateVectorDAO()
    val ubicacionDAO = HibernateUbicacionDAO()
    val ubicacionService = UbicacionServiceImpl(ubicacionDAO)
    val vectorServ = VectorServiceImpl(hibernate)


    @BeforeEach
    fun setUp() {

    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun contagiar() {
    }

    @Test
    fun infectar() {
    }

    @Test
    fun enfermedades() {
    }

    @Test
    fun cuandoSeCreaUnVectorSeLeAsignaUnId() {
        val ubic = ubicacionService.crearUbicacion("BSAS")
        val tipo = TipoDeVector.Persona
        var vector = vectorServ.crearVector(tipo,ubic.id!!)
        Assert.assertTrue(vector.id != null)
    }

    @Test
    fun recuperarVector() {
    }

    @Test
    fun borrarVector() {
    }

    @Test
    fun getUbicacionDAO() {
    }
}