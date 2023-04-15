package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EspecieServiceImplTest {

    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieService = EspecieServiceImpl(hibernateEspecieDAO)
    private val hibernatePatogenoDAO = HibernatePatogenoDAO()
    private val patogenoService = PatogenoServiceImpl(hibernatePatogenoDAO)
    var dataService = DataServiceHibernate()


    @BeforeEach
    fun crearModelo() {
        dataService.crearSetDeDatosIniciales()
    }


    @Test
    fun recuperarEspecie() {
        val patogeno = Patogeno("Virus")
        patogenoService.crearPatogeno(patogeno)

        val hibernateUbicacionDAO = HibernateUbicacionDAO()
        val ubicacion = UbicacionServiceImpl(hibernateUbicacionDAO)
        val ubicacionCreada = ubicacion.crearUbicacion("Argentina")


        val ubicacionRecuperada = ubicacion.recuperar(ubicacionCreada.id!!)


        val especieGenerada = patogenoService.agregarEspecie(patogeno.id!!,"EspecieViolenta", ubicacionRecuperada.id!!)
        val especieRecuperada = especieService.recuperarEspecie(especieGenerada.id!!)
        Assert.assertEquals(especieGenerada.id!!, especieRecuperada.id!!)
    }


    @Test
    fun cantidadDeInfectados() {
        var patogeno = Patogeno("Virus")
        patogeno = patogenoService.crearPatogeno(patogeno)
        var patogenoBacteria = Patogeno("Bacteria")
        patogeno = patogenoService.crearPatogeno(patogenoBacteria)

        var hibernateUbicacionDAO = HibernateUbicacionDAO()
        var ubicacion = UbicacionServiceImpl(hibernateUbicacionDAO)
        var ubicacionCreada = ubicacion.crearUbicacion("Argentina")
        var ubicacionRecuperada = ubicacion.recuperar(ubicacionCreada.id!!)



        var virusConEspecieViolenta = patogenoService.agregarEspecie(patogeno.id!!,"EspecieViolenta", ubicacionRecuperada.id!!)
        patogenoService.agregarEspecie(patogenoBacteria.id!!,"EspecieViolenta", ubicacionRecuperada.id!!)
        Assert.assertEquals(especieService.cantidadDeInfectados(virusConEspecieViolenta.id!!),2)
    }




    @Test
    fun recuperarTodas() {
    }

    @Test
    fun getHibernateEspecieDAO() {
    }
}