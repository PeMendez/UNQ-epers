package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.*
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.VectorService

import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class VectorServiceImplTest {

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
    fun crearVector() {
        val hibernate = HibernateVectorDAO()
        val ubicacion = HibernateUbicacionDAO()
        val vectorServ = VectorServiceImpl(hibernate,ubicacion)
        val ubic = Ubicacion("BSAS",3)
        val tipo = TipoDeVector.Persona

        var vector = vectorServ.crearVector(tipo,3)
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