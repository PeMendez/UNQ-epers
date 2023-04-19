package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EstadisticaServiceImplTest {

    var dataService = DataServiceHibernate()
    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieService = EspecieServiceImpl(hibernateEspecieDAO)
    private val hibernateVectorDAO = HibernateVectorDAO()
    private val vectorServiceImpl = VectorServiceImpl(hibernateVectorDAO)
    private val estadisticaService = EstadisticaServiceImpl()

    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun especieLider() {
        val especieQueMasInfecto = especieService.recuperarEspecie(1)
        val unVector = vectorServiceImpl.recuperarVector(3)

        vectorServiceImpl.infectar(unVector,especieQueMasInfecto)
        Assert.assertEquals(unVector.tipo, TipoDeVector.Persona)
        Assert.assertEquals(especieQueMasInfecto.id, estadisticaService.especieLider().id)
    }

    /*
    @Test
    fun lideres() {
        val especieInf = especieService.recuperarEspecie(2)
        val unVector = vectorServiceImpl.recuperarVector(3)
        val otroVector = vectorServiceImpl.recuperarVector(1)


        vectorServiceImpl.infectar(unVector,especieInf)
        vectorServiceImpl.infectar(otroVector,especieInf)

        Assert.assertEquals(unVector.tipo, TipoDeVector.Persona)
        Assert.assertEquals(otroVector.tipo, TipoDeVector.Animal)
        //Assert.assertEquals(especieInf.id, estadisticaService.especieLider().id)

    }

     */

    @Test
    fun reporteDeContagios() {
    }


    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}