package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteUnaEspecieLider
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EspecieServiceImplTest {

    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val especieService = EspecieServiceImpl(hibernateEspecieDAO)

    private var dataService = DataServiceHibernate()

    private val patogenoDAO = HibernatePatogenoDAO()
    private val patogenoService = PatogenoServiceImpl(patogenoDAO)

    private val ubicacionDAO = HibernateUbicacionDAO()
    private val ubicacionService = UbicacionServiceImpl(ubicacionDAO)

    private val vectorDAO = HibernateVectorDAO()
    private val vectorServiceImpl = VectorServiceImpl(vectorDAO)


    @BeforeEach
    fun crearModelo() {
       dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun noSePuedeRecuperarUnaEspecieConUnIdInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            especieService.recuperarEspecie(82828)
        }
    }

    @Test
    fun seRecuperaUnaEspecieConTodosSusDatosCorrectos() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada.id!!)

        val especieRecuperada = especieService.recuperarEspecie(especieCreada.id!!)

        Assertions.assertEquals(especieRecuperada.id!!, especieCreada.id!!)
        Assertions.assertEquals(especieRecuperada.nombre, especieCreada.nombre)
        Assertions.assertEquals(especieRecuperada.paisDeOrigen, ubicacionCreada.nombre)
        Assertions.assertEquals(especieRecuperada.patogeno.id, patogenoCreado.id!!)

    }

    @Test
    fun noSePuedeRecuperarLaCantidadDeInfectadosConUnIdDeEspecieInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            especieService.cantidadDeInfectados(238383494)
        }
    }

    @Test
    fun seRecuperanLaCantidadDeInfectadosCorrectamente() {
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorRecuperado = vectorServiceImpl.recuperarVector(1)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada.id!!)

        Assertions.assertEquals(1, especieService.cantidadDeInfectados(especieCreada.id!!))

        vectorServiceImpl.infectar(vectorRecuperado, especieCreada)

        Assertions.assertEquals(2, especieService.cantidadDeInfectados(especieCreada.id!!))
    }

    @Test
    fun seConoceLaEspecieLiderConMayorCantidadDePersonasInfectadasCorrectamente() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorPersonaCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        val patogeno2 = Patogeno("otroNombre")
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombre")
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada2.id!!)
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada2.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado2.id!!, "nombrenuevo", ubicacionCreada2.id!!)

        vectorServiceImpl.infectar(vectorAnimalCreado, especieCreada2)

        Assertions.assertEquals(especieService.cantidadDeInfectados(especieCreada2.id!!), 2)
        Assertions.assertEquals(especieService.cantidadDeInfectados(especieCreada1.id!!), 1)

        Assertions.assertEquals(especieService.especieLider().id!!, especieCreada1.id!!)
    }

    @Test
    fun alHaberSoloEspeciesConInfectadosNoPersonasEntoncesNoHayEspecieLider() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        val patogeno2 = Patogeno("otroNombre")
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombreDeUbicacion")
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada2.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        Assertions.assertEquals(especieService.cantidadDeInfectados(especieCreada1.id!!), 1)
        Assertions.assertEquals(especieService.cantidadDeInfectados(especieCreada2.id!!), 1)
        Assertions.assertThrows(NoExisteUnaEspecieLider::class.java) {
            especieService.especieLider()
        }
    }

    @Test
    fun alNoHaberEspeciesEnLaBDDEntoncesNoHayEspecieLider() {
        dataService.eliminarTodo()

        Assertions.assertThrows(NoExisteUnaEspecieLider::class.java) {
            especieService.especieLider()
        }
    }

    @Test
    fun seRecuperanTodasLasEspeciesCorrectamente() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        val patogeno2 = Patogeno("otroNombre")
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombreDeUbicacion")
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada2.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        val especiesRecuperadas = especieService.recuperarTodas()

        Assertions.assertNotNull(especiesRecuperadas.find { it.id == especieCreada1.id!! })
        Assertions.assertNotNull(especiesRecuperadas.find { it.id == especieCreada2.id!! })

        Assertions.assertEquals(2, especiesRecuperadas.size)
    }

    @Test
    fun alRecuperarTodasLasEspeciesConUnaBDDVaciaEntoncesSeRetornaUnaListaVacia() {
        dataService.eliminarTodo()

        val especiesRecuperadas = especieService.recuperarTodas()

        Assertions.assertTrue(especiesRecuperadas.isEmpty())
    }

    @Test
    fun seRecuperanCorrectamenteLosLideresConMayorCantidadDeInfectadosPersonasOAnimales() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        val patogeno2 = Patogeno("otroNombre")
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombreDeUbicacion")
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        val lideres = especieService.lideres()

        Assertions.assertNotNull(lideres.find { it.id == especieCreada1.id!! })
        Assertions.assertNotNull(lideres.find { it.id == especieCreada2.id!! })

        Assertions.assertEquals(2, lideres.size)
    }

    @Test
    fun siUnaEspecieSoloTieneInfectadosInsectosNoFormaParteDelResultadoFinal() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        val patogeno2 = Patogeno("otroNombre")
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombreDeUbicacion")
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada2.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        val lideres = especieService.lideres()

        Assertions.assertNotNull(lideres.find { it.id == especieCreada1.id!! })

        Assertions.assertEquals(1, lideres.size)
    }

    @Test
    fun siHayMasDeDiezEspeciesLideresSoloSeMuestranLasPrimerasDiez() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie1", ubicacionCreada1.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie2", ubicacionCreada1.id!!)
        val especieCreada3 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie3", ubicacionCreada1.id!!)
        val especieCreada4 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie4", ubicacionCreada1.id!!)
        val especieCreada5 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie5", ubicacionCreada1.id!!)
        val especieCreada6 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie6", ubicacionCreada1.id!!)
        val especieCreada7 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie7", ubicacionCreada1.id!!)
        val especieCreada8 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie8", ubicacionCreada1.id!!)
        val especieCreada9 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie9", ubicacionCreada1.id!!)
        val especieCreada10 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie10", ubicacionCreada1.id!!)
        val especieCreada11 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie11", ubicacionCreada1.id!!)
        val especieCreada12 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie12", ubicacionCreada1.id!!)

        val lideres = especieService.lideres()

        Assertions.assertEquals(10, lideres.size)
    }

    @Test
    fun losLideresEstanOrdenadosDeMayorAMenor() {
        dataService.eliminarTodo()
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        val vectorAnimalCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorAnimalCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorAnimalCreado2, especieCreada1)

        val patogeno2 = Patogeno("otroNombre")
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombreDeUbicacion")
        val vectorPersonaCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        val lideres = especieService.lideres()

        Assertions.assertEquals(2, especieService.cantidadDeInfectados(especieCreada1.id!!))
        Assertions.assertEquals(1, especieService.cantidadDeInfectados(especieCreada2.id!!))
        Assertions.assertEquals(especieCreada1.id!! ,lideres.first().id!!)
    }

    @Test
    fun siNoHayEspeciesEnLaBDDEntoncesAlRecuperarLideresSeRetornaListaVacia() {
        dataService.eliminarTodo()

        Assertions.assertTrue(especieService.lideres().isEmpty())
    }

    @AfterEach
    fun eliminarModelo() {
       dataService.eliminarTodo()
    }

}