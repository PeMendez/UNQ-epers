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
    fun unaEspecieEsEspecieLiderInfectandoPersonas() {
        val especieQueMasInfecto = especieService.recuperarEspecie(3)
        val unVector = vectorServiceImpl.recuperarVector(3)

        vectorServiceImpl.infectar(unVector,especieQueMasInfecto)
        Assert.assertEquals(unVector.tipo, TipoDeVector.Persona)
        Assert.assertEquals(especieQueMasInfecto.id, estadisticaService.especieLider().id)
    }


    @Test
    fun lideresQueInfectaronLaMayorCantidadDeHumanosYAnimales() {

        val especiePeron = especieService.recuperarEspecie(1)
        val especieMacrista = especieService.recuperarEspecie(10)
        val vectorCristina = vectorServiceImpl.recuperarVector(12)
        val vectorNestor = vectorServiceImpl.recuperarVector(11)
        val vectorDemente = vectorServiceImpl.recuperarVector(10)
        val vectorMacrista = vectorServiceImpl.recuperarVector(9)
        val vectorChristianGrey = vectorServiceImpl.recuperarVector(13)

        vectorServiceImpl.infectar(vectorCristina, especiePeron)
        vectorServiceImpl.infectar(vectorNestor, especiePeron)
        vectorServiceImpl.infectar(vectorDemente, especiePeron)
        vectorServiceImpl.infectar(vectorMacrista, especiePeron)
        vectorServiceImpl.infectar(vectorChristianGrey, especiePeron)

        Assert.assertEquals(estadisticaService.lideres().size, 10)
        Assert.assertEquals(estadisticaService.lideres().first().id, especiePeron.id)
        Assert.assertEquals(estadisticaService.lideres().last().id, especieMacrista.id)

    }


    @Test
    fun reporteDeContagios() {
        val reporte = estadisticaService.reporteDeContagios("ubicacion1")
        Assert.assertEquals(reporte.vectoresInfectados, 1)
        Assert.assertEquals(reporte.vectoresPresentes, 7)
        Assert.assertEquals(reporte.nombreDeEspecieMasInfecciosa, "especie11")
    }


    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}