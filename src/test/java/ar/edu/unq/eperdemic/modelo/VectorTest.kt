package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.EspecieServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ar.edu.unq.eperdemic.utils.DataServiceHibernate


class VectorTest {

    private val hibernateVectorDAO = HibernateVectorDAO()
    private val hibernateUbicacionDAO = HibernateUbicacionDAO()
    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val ubicacionServiceImpl = UbicacionServiceImpl(hibernateUbicacionDAO)
    private val vectorServiceImpl = VectorServiceImpl(hibernateVectorDAO)
    private val especieServiceImpl = EspecieServiceImpl(hibernateEspecieDAO)
    private val dataServiceHibernate = DataServiceHibernate()

    @BeforeEach
    fun setUp() {
        dataServiceHibernate.crearSetDeDatosIniciales()
    }

    @AfterEach
    fun tearDown() {
        dataServiceHibernate.eliminarTodo()
    }

    @Test
    fun intentarInfectar() {
    }

    @Test
    fun infectarCon() {
    }

    @Test
    fun esContagioExitosoTrue() {
        val vectorPersonaSano = vectorServiceImpl.recuperarVector(7)
        val vectorPersonaEnfermo = vectorServiceImpl.recuperarVector(14)
        val especie = especieServiceImpl.recuperarEspecie(1)
        vectorServiceImpl.infectar(vectorPersonaEnfermo, especie)

        assertEquals(vectorPersonaSano.ubicacion.nombre,vectorPersonaEnfermo.ubicacion.nombre)

        //val vectoresAInfectar = listOf(vectorPersonaSano)
        //assertTrue(vectorPersonaSano.estaSano())
        //assertFalse(vectorPersonaEnfermo.estaSano())
        //vectorServiceImpl.contagiar(vectorPersonaEnfermo,vectoresAInfectar)
        //assertFalse(vectorPersonaSano.estaSano())
    }
    @Test
    fun esContagioExitosoFalse() {

    }
    @Test
    fun porcentajeDeContagioExitoso() {
    }

    @Test
    fun estaSanoTrue() {
        val vectorSano = vectorServiceImpl.recuperarVector(7)
        assertTrue(vectorSano.estaSano())
    }
    @Test
    fun estaSanoFalse() {
        val vectorAInfectar = vectorServiceImpl.recuperarVector(7)
        val especie = especieServiceImpl.recuperarEspecie(1)
        assertTrue(vectorAInfectar.estaSano())
        vectorAInfectar.infectarCon(especie)
        assertFalse(vectorAInfectar.estaSano())
    }

    @Test
    fun tieneEfermedad() {
    }

    @Test
    fun mover() {
    }
}