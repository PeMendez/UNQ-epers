package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.*

class VectorServiceImplTest {

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
    }

    @Test
    fun contagiar() {
    }

    @Test
    fun infectar() {
        val unVector = vectorServiceImpl.recuperarVector(1)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)
        val otraEspecie = especieServiceImpl.recuperarEspecie(2)

        vectorServiceImpl.infectar(unVector,unaEspecie)

        Assertions.assertTrue(unVector.especies.contains(unaEspecie))
        Assertions.assertFalse(unVector.especies.contains(otraEspecie))
    }

    @Test
    fun enfermedades() {
        val unVector = vectorServiceImpl.recuperarVector(1)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)
        val otraEspecie = especieServiceImpl.recuperarEspecie(2)
        val otraEspecieMas = especieServiceImpl.recuperarEspecie(3)
        val unVectorId = unVector.id!!

        vectorServiceImpl.infectar(unVector,unaEspecie)
        vectorServiceImpl.infectar(unVector,otraEspecie)

        Assertions.assertTrue(vectorServiceImpl.enfermedades(unVectorId).contains(unaEspecie))
        Assertions.assertTrue(vectorServiceImpl.enfermedades(unVectorId).contains(otraEspecie))
        Assertions.assertFalse(vectorServiceImpl.enfermedades(unVectorId).contains(otraEspecieMas))
    }

    @Test
    fun cuandoSeCreaUnVectorSeLeAsignaUnId() {
        val ubic = ubicacionServiceImpl.crearUbicacion("BSAS")
        val tipo = TipoDeVector.Persona
        var vector = vectorServiceImpl.crearVector(tipo,ubic.id!!)
        Assert.assertTrue(vector.id != null)
    }

    @Test
    fun recuperarVector() {
        val vectorRecuperado = vectorServiceImpl.recuperarVector(1)
        Assert.assertEquals(vectorRecuperado.ubicacion.nombre, "ubicacion1")
        Assert.assertEquals(vectorRecuperado.id!!, 1)
        Assert.assertEquals(vectorRecuperado.tipo, TipoDeVector.Animal)
    }


    @Test
    fun borrarVector() {
        val ubic = ubicacionServiceImpl.crearUbicacion("BSAS")
        val tipo = TipoDeVector.Persona
        var vector = vectorServiceImpl.crearVector(tipo,ubic.id!!)
        vectorServiceImpl.borrarVector(vector.id!!)
        Assert.assertNull(vectorServiceImpl.recuperarVector(vector.id!!))
    }

    @Test
    fun getUbicacionDAO() {
    }
}