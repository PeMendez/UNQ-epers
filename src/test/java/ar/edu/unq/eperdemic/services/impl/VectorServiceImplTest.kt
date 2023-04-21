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

    @Test
    fun contagiar() {
        val unVector = vectorServiceImpl.recuperarVector(3)
        val otroVector = vectorServiceImpl.recuperarVector(4)
        val otroVectorMas = vectorServiceImpl.recuperarVector(5)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(unVector,unaEspecie)

        val vectores = listOf(otroVector,otroVectorMas)

        //reviso que los vectores a contagiar no tengan la especie de id 1
        Assertions.assertFalse(otroVector.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertFalse(otroVectorMas.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(unVector,vectores)

        //reviso que los vectores a contagiar ahora si tengan la especie de id 1
        Assertions.assertTrue(otroVector.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertTrue(otroVectorMas.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun infectar() {
        val unVector = vectorServiceImpl.recuperarVector(1)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(unVector,unaEspecie)

        Assertions.assertTrue(unVector.especies.contains(unaEspecie))
    }

    @Test
    fun enfermedades() {

        val unVector = vectorServiceImpl.recuperarVector(1)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)
        vectorServiceImpl.infectar(unVector,unaEspecie)
        val enfermedades = vectorServiceImpl.enfermedades(unVector.id!!)

        Assertions.assertEquals(enfermedades.size, 1)

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
        vectorServiceImpl.borrarVector(1)
        Assert.assertNull(vectorServiceImpl.recuperarVector(1))
    }

    @AfterEach
    fun eliminarModelo() {
        dataServiceHibernate.eliminarTodo()
    }
}