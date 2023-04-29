package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.jupiter.api.*

class VectorServiceImplTest {

    private val hibernateVectorDAO = HibernateVectorDAO()
    private val hibernateUbicacionDAO = HibernateUbicacionDAO()
    private val hibernateEspecieDAO = HibernateEspecieDAO()
    private val patogenoDAO = HibernatePatogenoDAO()

    private val ubicacionServiceImpl = UbicacionServiceImpl(hibernateUbicacionDAO)
    private val vectorServiceImpl = VectorServiceImpl(hibernateVectorDAO)
    private val especieServiceImpl = EspecieServiceImpl(hibernateEspecieDAO)
    private val dataServiceHibernate = DataServiceHibernate()
    private val patogenoService = PatogenoServiceImpl(patogenoDAO)

    @BeforeEach
    fun setUp() {
        dataServiceHibernate.crearSetDeDatosIniciales()
    }

    @Test
    fun siSeIntentanContagiarVectoresConUnVectorConIdInexistenteFalla() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vector1 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)

        dataServiceHibernate.eliminarTodo()

        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest2")
        val vector2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada2.id!!)

        val vectores = listOf(vector2)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.contagiar(vector1, vectores)
        }
    }

    @Test
    fun siSeIntentanContagiarVectoresConUnVectorInvalidoEntoncesFalla() {
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest2")
        val vector2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vector1 = Vector(TipoDeVector.Persona, ubicacionCreada2)

        val vectores = listOf(vector2)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.contagiar(vector1, vectores)
        }
    }

    @Test
    fun siSeIntectaContagiarAUnaListaDeVectoresVaciaEntoncesNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorInsectoCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorInsectoCreado1, unaEspecie)

        val vectores = emptyList<Vector>()

        vectorServiceImpl.contagiar(vectorInsectoCreado1, vectores)

        Assertions.assertTrue(vectorInsectoCreado1.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun soloSePuedenContagiarVectoresInsectosDeVectoresPersonaOAnimalDeLoContrarioNoHaceNada() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorInsectoCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val vectorInsectoCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorInsectoCreado1, unaEspecie)
        vectorServiceImpl.infectar(vectorAnimalCreado, unaEspecie)

        val vectores = listOf(vectorInsectoCreado2)

        Assertions.assertFalse(vectorInsectoCreado2.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertEquals(vectorInsectoCreado1.ubicacion.id!!, vectorInsectoCreado2.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorInsectoCreado1, vectores)

        Assertions.assertFalse(vectorInsectoCreado2.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorAnimalCreado, vectores)

        Assertions.assertTrue(vectorInsectoCreado2.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun losVectoresPersonaSePuedenContagiarDeCualquierVector() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorPersonaCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorPersonaCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val especie1 = especieServiceImpl.recuperarEspecie(1)
        val especie2 = especieServiceImpl.recuperarEspecie(2)
        val especie3 = especieServiceImpl.recuperarEspecie(3)


        vectorServiceImpl.infectar(vectorPersonaCreado2, especie1)
        vectorServiceImpl.infectar(vectorAnimalCreado, especie2)
        vectorServiceImpl.infectar(vectorInsectoCreado, especie3)

        val vectores = listOf(vectorPersonaCreado1)

        Assertions.assertFalse(vectorPersonaCreado1.tieneEfermedad(especie1.id!!))
        Assertions.assertFalse(vectorPersonaCreado1.tieneEfermedad(especie2.id!!))
        Assertions.assertFalse(vectorPersonaCreado1.tieneEfermedad(especie3.id!!))
        Assertions.assertEquals(vectorPersonaCreado1.ubicacion.id!!, vectorPersonaCreado2.ubicacion.id!!)
        Assertions.assertEquals(vectorPersonaCreado1.ubicacion.id!!, vectorAnimalCreado.ubicacion.id!!)
        Assertions.assertEquals(vectorPersonaCreado1.ubicacion.id!!, vectorInsectoCreado.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorPersonaCreado2, vectores)
        vectorServiceImpl.contagiar(vectorAnimalCreado, vectores)
        vectorServiceImpl.contagiar(vectorInsectoCreado, vectores)

        Assertions.assertTrue(vectorPersonaCreado1.tieneEfermedad(especie1.id!!))
        Assertions.assertTrue(vectorPersonaCreado1.tieneEfermedad(especie2.id!!))
        Assertions.assertTrue(vectorPersonaCreado1.tieneEfermedad(especie3.id!!))
    }

    @Test
    fun soloSePuedenContagiarAVectoresAnimalesConVectoresInsectoDeLoContrarioNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorAnimalCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorAnimalCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorPersonaCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorAnimalCreado2, unaEspecie)
        vectorServiceImpl.infectar(vectorPersonaCreado, unaEspecie)
        vectorServiceImpl.infectar(vectorInsectoCreado, unaEspecie)

        val vectores = listOf(vectorAnimalCreado1)

        Assertions.assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorAnimalCreado2.ubicacion.id!!)
        Assertions.assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorPersonaCreado.ubicacion.id!!)
        Assertions.assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorInsectoCreado.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorAnimalCreado2, vectores)
        Assertions.assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorPersonaCreado, vectores)
        Assertions.assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorInsectoCreado, vectores)
        Assertions.assertTrue(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun siSeIntentaContagiarAVectoresEnOtraUbicacionEntoncesNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest2")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorCreado1, unaEspecie)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        Assertions.assertFalse(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertFalse(vectorCreado3.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertNotEquals(vectorCreado1.ubicacion.id!!, vectorCreado2.ubicacion.id!!)
        Assertions.assertNotEquals(vectorCreado1.ubicacion.id!!, vectorCreado3.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertFalse(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertFalse(vectorCreado3.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun siUnVectorNoTieneEnfermedadesYSeIntentaContagiarAOtrosVectoresEntoncesNoSeHaceNada() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertTrue(vectorCreado2.estaSano())
        Assertions.assertTrue(vectorCreado3.estaSano())

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertTrue(vectorCreado2.estaSano())
        Assertions.assertTrue(vectorCreado3.estaSano())
    }

    @Test
    fun seContagianCorrectamenteALosVectoresConUnaEnfermedad() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorCreado1, unaEspecie)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        Assertions.assertFalse(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertFalse(vectorCreado3.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertTrue(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertTrue(vectorCreado3.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun seContagianCorrectamenteALosVectoresConMasDeUnaEnfermedad() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieRecuperada1 = especieServiceImpl.recuperarEspecie(1)
        val especieRecuperada2 = especieServiceImpl.recuperarEspecie(2)

        vectorServiceImpl.infectar(vectorCreado1, especieRecuperada1)
        vectorServiceImpl.infectar(vectorCreado1, especieRecuperada2)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        Assertions.assertFalse(vectorCreado2.tieneEfermedad(especieRecuperada1.id!!))
        Assertions.assertFalse(vectorCreado3.tieneEfermedad(especieRecuperada1.id!!))
        Assertions.assertFalse(vectorCreado2.tieneEfermedad(especieRecuperada2.id!!))
        Assertions.assertFalse(vectorCreado3.tieneEfermedad(especieRecuperada2.id!!))

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertTrue(vectorCreado2.tieneEfermedad(especieRecuperada1.id!!))
        Assertions.assertTrue(vectorCreado3.tieneEfermedad(especieRecuperada1.id!!))
        Assertions.assertTrue(vectorCreado2.tieneEfermedad(especieRecuperada2.id!!))
        Assertions.assertTrue(vectorCreado3.tieneEfermedad(especieRecuperada2.id!!))
    }

    @Test fun noSePuedeCrearUnaEspecieConNombreVacioOConCaracteresEspeciales() {
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie#1", ubicacionCreada1.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie-1", ubicacionCreada1.id!!)
        }
    }

    @Test fun noSePuedeCrearUnaEspecieConNombreDePaisDeOrigenVacioOConCaracteresEspeciales() {
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionCreada1.nombre = ""
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionCreada1.nombre = "ubicacion#88"
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionCreada1.nombre = "@ubicacion-1"
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }
    }

    @Test
    fun seInfectaAUnVectorConUnaEspecieCorrectamente() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        Assertions.assertTrue(vectorServiceImpl.enfermedades(vectorCreado.id!!).isEmpty())

        vectorServiceImpl.infectar(vectorCreado, especieRecuperada)

        val enfermedades = vectorServiceImpl.enfermedades(vectorCreado.id!!)

        Assertions.assertNotNull(enfermedades.find {
            it.id == especieRecuperada.id!! &&
                    it.nombre == especieRecuperada.nombre &&
                    it.paisDeOrigen == especieRecuperada.paisDeOrigen &&
                    it.patogeno.id!! == especieRecuperada.patogeno.id!!
        })
        Assertions.assertEquals(1, enfermedades.size)
    }

    @Test
    fun noSePuedeInfectarAUnVectorInexistenteEnLaBDDConUnaEspecie() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        dataServiceHibernate.eliminarTodo()

        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado1, especieCreada1)
        }
    }

    @Test
    fun noSePuedeInfectarAUnVectorConUnaEspecieInexistenteEnLaBDD() {
        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        dataServiceHibernate.eliminarTodo()

        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado, especieRecuperada)
        }
    }

    @Test
    fun noSeLePuedePasarAInfectarUnObjetoVectorSinID() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        val vector = Vector(TipoDeVector.Persona, ubicacionCreada1)
        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vector, especieRecuperada)
        }
    }

    @Test
    fun noSeLePuedePasarAInfectarUnObjetoEspecieSinID() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especie = Especie(patogenoCreado, "testEspecie", "unPaisCualquiera")

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado, especie)
        }
    }

    @Test
    fun seRecuperanLasEnfermedadesDeUnVectorCorrectamenteYAlEstarSanoRetornaUnaListaVacia() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertTrue(vectorServiceImpl.enfermedades(vectorCreado.id!!).isEmpty())

        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorCreado, especieRecuperada)

        val enfermedades = vectorServiceImpl.enfermedades(vectorCreado.id!!)

        Assertions.assertNotNull(enfermedades.find {
            it.id == especieRecuperada.id!! &&
            it.nombre == especieRecuperada.nombre &&
            it.paisDeOrigen == especieRecuperada.paisDeOrigen &&
            it.patogeno.id!! == especieRecuperada.patogeno.id!!
        })
        Assertions.assertEquals(1, enfermedades.size)
    }

    @Test
    fun noSePuedenRecuperarLasEnfermedadesDeUnVectorInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.enfermedades(12312313)
        }
    }

    @Test
    fun cuandoSeCreaUnVectorSeLeAsignaUnId() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("idVectorTest")
        val vector = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertNotNull(vector.id)
    }

    @Test
    fun noSePuedeCrearUnVectorConUnaUbicacionInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.crearVector(TipoDeVector.Persona, 12312313)
        }
    }

    @Test
    fun seRecuperaUnVectorConTodosSusDatosCorrectamente() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("testRecuperarVector")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada.id!!)
        val vectorRecuperado = vectorServiceImpl.recuperarVector(vectorCreado.id!!)

        Assertions.assertEquals(vectorRecuperado.ubicacion.id!!, vectorCreado.ubicacion.id!!)
        Assertions.assertEquals(vectorRecuperado.id!!, vectorCreado.id!!)
        Assertions.assertEquals(vectorRecuperado.tipo, vectorCreado.tipo)
        Assertions.assertEquals(vectorRecuperado.especies.size, vectorCreado.especies.size)
    }

    @Test
    fun noSePuedeRecuperarUnVectorConUnIdInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.recuperarVector(29129229)
        }
    }

    @Test
    fun seBorraUnVectorCorrectamenteDeLaBDD() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("testBorrarVector")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada.id!!)

        vectorServiceImpl.borrarVector(vectorCreado.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.recuperarVector(vectorCreado.id!!)
        }
    }

    @Test
    fun noSePuedeBorrarUnVectorDeLaBDDConUnIdInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.borrarVector(29129229)
        }
    }

    @Test
    fun seRecuperanTodosLosVectoresCorrectamente() {
        dataServiceHibernate.eliminarTodo()

        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("nombreCualquiera1")
        val vector1Creado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vector2Creado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        val vectoresRecuperados = vectorServiceImpl.recuperarTodos()

        Assertions.assertNotNull(vectoresRecuperados.find { it.id == vector1Creado.id!! })
        Assertions.assertNotNull(vectoresRecuperados.find { it.id == vector2Creado.id!! })

        Assertions.assertTrue(vectoresRecuperados.size == 2)
    }

    @Test
    fun alRecuperarTodosLosVectoresDeUnaBDDVaciaEntoncesSeRetornaUnaListaVacia() {
        dataServiceHibernate.eliminarTodo()

        val vectoresRecuperados = vectorServiceImpl.recuperarTodos()

        Assertions.assertTrue(vectoresRecuperados.isEmpty())
    }

    @AfterEach
    fun eliminarModelo() {
        dataServiceHibernate.eliminarTodo()
    }
}