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
    fun esContagioExitoso() {

    }

    @Test
    fun porcentajeDeContagioExitoso() {
    }

    @Test
    fun estaSano() {
        val vector = vectorServiceImpl.recuperarVector(7)
        val especie = especieServiceImpl.recuperarEspecie(1)
        assertTrue(vector.estaSano())
        vector.infectarCon(especie)
        assertFalse(vector.estaSano())
    }

    @Test
    fun tieneEfermedad() {
    }

    @Test
    fun mover() {
    }
}