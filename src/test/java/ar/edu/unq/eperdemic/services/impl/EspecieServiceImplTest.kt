package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EspecieServiceImplTest {

    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieService = EspecieServiceImpl(hibernateEspecieDAO)
    var dataService = DataServiceHibernate()


    @BeforeEach
    fun crearModelo() {
       dataService.crearSetDeDatosIniciales()
    }


    @Test
    fun recuperarEspecie() {

        val especieRecuperada = especieService.recuperarEspecie(1)

        Assertions.assertEquals(especieRecuperada.id, 1)
        Assertions.assertEquals(especieRecuperada.nombre, "especie1")
        Assertions.assertEquals(especieRecuperada.paisDeOrigen, "ubicacion1")
        Assertions.assertEquals(especieRecuperada.patogeno.id, 1)

    }


    //@Test
    fun cantidadDeInfectados() {

        Assert.assertEquals(especieService.cantidadDeInfectados(1),2)
    }


    @Test
    fun recuperarTodas() {

        val especies = especieService.recuperarTodas()

        Assertions.assertNotNull(especies.find { it.id == 1.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 2.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 3.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 4.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 5.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 6.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 7.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 8.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 9.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 10.toLong() })
        Assertions.assertNotNull(especies.find { it.id == 11.toLong() })

        Assertions.assertTrue(especies.size == 11)

    }


    @AfterEach
    fun eliminarModelo() {
       dataService.eliminarTodo()
    }

}