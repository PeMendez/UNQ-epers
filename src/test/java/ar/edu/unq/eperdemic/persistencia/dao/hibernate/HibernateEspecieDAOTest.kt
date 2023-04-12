package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.services.impl.EspecieServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.*

class HibernateEspecieDAOTest {

    lateinit var especie: Especie
    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieService = EspecieServiceImpl(hibernateEspecieDAO)


    @BeforeEach
    fun setUp() {

    }

    @Test
    fun recuperarEspecie() {
    }

    @Test
    fun cantidadDeInfectados() {
    }

    @Test
    fun recuperarTodas() {
    }
}