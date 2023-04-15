package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VectorServiceImplTest {

    val hibernate = HibernateVectorDAO()
    val ubicacionDAO = HibernateUbicacionDAO()
    val ubicacionService = UbicacionServiceImpl(ubicacionDAO)
    val vectorServ = VectorServiceImpl(hibernate)
    var dataService = DataServiceHibernate()

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
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
        val vectorRecuperado = vectorServ.recuperarVector(1)
        Assert.assertEquals(vectorRecuperado.ubicacion.nombre, "ubicacion1")
        Assert.assertEquals(vectorRecuperado.id!!, 1)
        Assert.assertEquals(vectorRecuperado.tipo, TipoDeVector.Animal)
    }


    @Test
    fun borrarVector() {
        val ubic = ubicacionService.crearUbicacion("BSAS")
        val tipo = TipoDeVector.Persona
        var vector = vectorServ.crearVector(tipo,ubic.id!!)
        vectorServ.borrarVector(vector.id!!)
        Assert.assertNull(vectorServ.recuperarVector(vector.id!!))
    }

    @Test
    fun getUbicacionDAO() {
    }
}