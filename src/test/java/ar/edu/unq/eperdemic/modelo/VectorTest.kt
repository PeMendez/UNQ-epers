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

    @Test
    fun alInfectarUnVectorConUnaEspecieEstaSeGuarda() {
        val vector = vectorServiceImpl.recuperarVector(1)
        val especie = especieServiceImpl.recuperarEspecie(1)

        vector.infectarCon(especie)

        assertTrue(vector.especies.contains(especie))

    }

    @Test
    fun alIntentarInfectarUnVectorConUnaEspecieQueYaEstaInfectadoNoSeDuplica() {
        val vector = vectorServiceImpl.recuperarVector(1)
        val especie = especieServiceImpl.recuperarEspecie(1)

        vector.infectarCon(especie)

        assertEquals(vector.especies.size, 1)

        vector.infectarCon(especie)

        assertEquals(vector.especies.size, 1)

    }

    @Test
    fun esContagioExitosoTrue() {
        val vectorPersonaSano = vectorServiceImpl.recuperarVector(6)
        val vectorPersonaEnfermo = vectorServiceImpl.recuperarVector(15)
        val especie = especieServiceImpl.recuperarEspecie(1)
        vectorServiceImpl.infectar(vectorPersonaEnfermo, especie)

        assertEquals(vectorPersonaSano.ubicacion.nombre, vectorPersonaEnfermo.ubicacion.nombre)
        assertEquals(vectorPersonaSano.tipo, vectorPersonaEnfermo.tipo)

        val vectoresAInfectar = listOf(vectorPersonaSano)
        assertTrue(vectorPersonaSano.estaSano())
        assertFalse(vectorPersonaEnfermo.estaSano())
        vectorServiceImpl.contagiar(vectorPersonaEnfermo, vectoresAInfectar)
        assertFalse(vectorPersonaSano.estaSano())
    }

    @Test
    fun esContagioExitosoFalsePorNoEstarEnLaMismaUbicacion() {
        val vectorPersonaSano = vectorServiceImpl.recuperarVector(6)
        val vectorPersonaEnfermo = vectorServiceImpl.recuperarVector(1)
        val especie = especieServiceImpl.recuperarEspecie(1)
        vectorServiceImpl.infectar(vectorPersonaEnfermo, especie)

        assertNotEquals(vectorPersonaSano.ubicacion.nombre, vectorPersonaEnfermo.ubicacion.nombre)

        val vectoresAInfectar = listOf(vectorPersonaSano)
        assertTrue(vectorPersonaSano.estaSano())
        assertFalse(vectorPersonaEnfermo.estaSano())
        vectorServiceImpl.contagiar(vectorPersonaEnfermo, vectoresAInfectar)
        assertTrue(vectorPersonaSano.estaSano())

    }

    @Test
    fun esContagioExitosoFalsePorNoEstarSerCompatibleDeTipo() {
        val vectorInsecto = vectorServiceImpl.recuperarVector(2)
        val vectorAnimalEnfermo = vectorServiceImpl.recuperarVector(1)
        val especie = especieServiceImpl.recuperarEspecie(1)
        vectorServiceImpl.infectar(vectorAnimalEnfermo, especie)

        assertTrue(vectorInsecto.estaSano())

        val vectoresAInfectar = listOf(vectorInsecto)

        assertFalse(vectorAnimalEnfermo.estaSano())
        vectorServiceImpl.contagiar(vectorAnimalEnfermo, vectoresAInfectar)
        assertTrue(vectorInsecto.estaSano())

    }

    @Test
    fun porcentajeDeContagioExitoso() {
        val especie = especieServiceImpl.recuperarEspecie(1)
        val vector = vectorServiceImpl.recuperarVector(6)

        assertEquals(vector.porcentajeDeContagioExitoso(especie), 110)
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
        val vectorSano = vectorServiceImpl.recuperarVector(6)
        val especie = especieServiceImpl.recuperarEspecie(1)

        assertTrue(vectorSano.estaSano())

        vectorServiceImpl.infectar(vectorSano, especie)

        assertTrue(vectorSano.tieneEfermedad(especie.id!!))
    }

    @Test
    fun cuandoSeMueveUnVectorSeLeModificaSuUbicacion() {
        val vector = vectorServiceImpl.recuperarVector(1)
        val ubicacion = ubicacionServiceImpl.recuperar(2)

        assertEquals(vector.ubicacion.nombre, "ubicacion1")

        vector.mover(ubicacion)

        assertEquals(vector.ubicacion.nombre, "ubicacion2")

    }

    @AfterEach
    fun tearDown() {
        dataServiceHibernate.eliminarTodo()
    }
}