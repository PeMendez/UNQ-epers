package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.Neo4jUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionServiceImplTest {

    @Autowired
    private lateinit var dataService: DataService
    @Autowired
    private lateinit var vectorService: VectorServiceImpl
    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl
    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl

    @Autowired
    private lateinit var ubicacionDAO: UbicacionDAO
    @Autowired
    private lateinit var neo4jUbicacionDAO: Neo4jUbicacionDAO


    //@BeforeEach
    fun setUp() {
       dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun seGuardaUnaUbicacionCorrectamente() {
        val ubicacionAGuardar = ubicacionService.crearUbicacion("testGuardarUbi")

        val ubicacionRecuperada = ubicacionDAO.recuperarUbicacionPorNombre(ubicacionAGuardar.nombre)

        Assertions.assertNotNull(ubicacionRecuperada.id)
    }

    @Test
    fun noSePuedeGuardarUnaUbicacionConUnNombreYaExistenteEnLaBDD() {
        ubicacionService.crearUbicacion("nombreIgual")

        Assertions.assertThrows(NombreDeUbicacionRepetido::class.java) {
            ubicacionService.crearUbicacion("nombreIgual")
        }
    }

    @Test
    fun alRecuperarTodasLasUbicacionesDeUnaBDDVaciaEntoncesSeRetornaUnaListaVacia() {
        dataService.eliminarTodo()
        val todasLasUbicaciones = ubicacionService.recuperarTodos()
        Assertions.assertTrue(todasLasUbicaciones.isEmpty())
    }

    @Test
    fun noSePuedeCrearUnaUbicacionConUnStringVacio() {
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionService.crearUbicacion("")
        }
    }

    @Test
    fun noSePuedeCrearUnaUbicacionConCaracteresEspeciales() {
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionService.crearUbicacion("ubicacion#1")
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionService.crearUbicacion("ubicacion-2")
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionService.crearUbicacion("@ubicacion3")
        }
    }


    @Test
    fun unaUbicacionCreadaTieneUnIdGenerado() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testUbicacion")

        Assertions.assertNotNull(ubicacionCreada.id)
    }
    @Test
    fun noSePuedeRecuperarUnaUbicacionConUnIdInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            ubicacionService.recuperar(-777)
        }
    }


    @Test
    fun unaUbicacionCreadaDeCeroNoTieneVectores() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testVectores")

        Assertions.assertTrue(ubicacionService.recuperarVectores(ubicacionCreada.id!!).isEmpty())
    }


    @Test
    fun seRecuperaUnaUbicacionConTodosSusDatosCorrectos() {
        val ubicacionCreada = ubicacionService.crearUbicacion("nombreATestear")
        val ubicacionRecuperada = ubicacionService.recuperar(ubicacionCreada.id!!)

        Assertions.assertEquals(ubicacionCreada.nombre, ubicacionRecuperada.nombre)
        Assertions.assertEquals(ubicacionCreada.id!!, ubicacionRecuperada.id!!)
    }

    @Test
    fun seRecuperanLosVectoresDeUnaUbicacionCorrectamente() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testVectores1")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testVectores2")
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)

        Assertions.assertTrue(ubicacionService.recuperarVectores(ubicacionCreada1.id!!).isEmpty())

        ubicacionService.mover(vectorCreado.id!!, ubicacionCreada1.id!!)

        Assertions.assertTrue(ubicacionService.recuperarVectores(ubicacionCreada1.id!!).size == 1)
    }

    @Test
    fun noPuedenExistirDosUbicacionesConElMismoNombre() {
        ubicacionService.crearUbicacion("mismoNombreTest")
        try {
            ubicacionService.crearUbicacion("mismoNombreTest")
            fail("Debería haber lanzado una excepción de restricción única")
        } catch (ex: Exception) {
            Assertions.assertTrue(ex is NombreDeUbicacionRepetido)
            Assertions.assertEquals("Ya existe una ubicacion con ese nombre.", ex.message)
        }
    }


    @Test
    fun puedenExistirDosUbicacionesConNombresDistintos() {
        val ubicacion1 = ubicacionService.crearUbicacion("nombreDistinto")
        try {
            val ubicacion2 = ubicacionService.crearUbicacion("otroNombre")
            Assertions.assertNotEquals(ubicacion1.nombre, ubicacion2.nombre)
        } catch (ex: NombreDeUbicacionRepetido) {
            fail("No tendria que haber lanzado una excepcion porque son distintos nombres")
        }
    }


    @Test
    fun seRecuperanTodasLasUbicacionesDeManeraCorrecta() {
        dataService.eliminarTodo()

        val ubicacionCreada1 = ubicacionService.crearUbicacion("nombreCualquiera1")
        val ubicacionCreada2 =ubicacionService.crearUbicacion("nombreCualquiera2")
        val ubicacionCreada3 =ubicacionService.crearUbicacion("nombreCualquiera3")

        val ubicacionesRecuperadas = ubicacionService.recuperarTodos()

        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == ubicacionCreada1.id!! })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == ubicacionCreada2.id!! })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == ubicacionCreada3.id!! })

        Assertions.assertTrue(ubicacionesRecuperadas.size == 3)

    }


    @Test
    fun noSePuedeMoverUnVectorAUnaUbicacionInexistente() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testVectores1")
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        Assertions.assertThrows(NoExisteElid::class.java) {
            ubicacionService.mover(vectorCreado.id!!, -777)
        }
    }


    @Test
    fun noSePuedeMoverUnVectorInexistenteAUnaUbicacion() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testVectores1")
        Assertions.assertThrows(NoExisteElid::class.java) {
            ubicacionService.mover(204, ubicacionCreada1.id!!)
        }
    }


    @Test
    fun siUnVectorYaSeEncuentraEnUnaUbicacionEntoncesAlMoverNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("nombre1")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        vectorService.infectar(vectorCreado2, especieCreada)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertFalse(vectorCreado2.estaSano())
        Assertions.assertEquals(ubicacionService.recuperarVectores(ubicacionCreada1.id!!).size, 2)

        ubicacionService.mover(vectorCreado2.id!!, ubicacionCreada1.id!!)

        val vectorActualizado1 = vectorService.recuperarVector(vectorCreado1.id!!)
        val vectorActualizado2 = vectorService.recuperarVector(vectorCreado2.id!!)

        Assertions.assertTrue(vectorActualizado1.estaSano())
        Assertions.assertEquals(vectorCreado2.especies.size, vectorActualizado2.especies.size)
        Assertions.assertEquals(ubicacionService.recuperarVectores(ubicacionCreada1.id!!).size, 2)
    }



    @Test
    fun seMueveUnVectorAUnaUbicacionCorrectamente() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMover1")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMover2")
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        Assertions.assertTrue(vectorCreado.ubicacion.id != ubicacionCreada2.id)

        ubicacionService.mover(vectorCreado.id!!, ubicacionCreada2.id!!)

        val vectorActualizado = vectorService.recuperarVector(vectorCreado.id!!)

        Assertions.assertEquals(vectorActualizado.ubicacion.id, ubicacionCreada2.id)
        Assertions.assertEquals(vectorActualizado.ubicacion.nombre, ubicacionCreada2.nombre)
    }


    @Test
    fun alMoverUnVectorInfectadoAUnaUbicacionEntoncesSeInfectaUnVectorAlAzar() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMoverInfectar1")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMoverInfectar2")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)

        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada3 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada3.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada3.id!!)

        vectorService.infectar(vectorCreado2, especieCreada)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertFalse(vectorCreado2.estaSano())

        ubicacionService.mover(vectorCreado2.id!!, ubicacionCreada1.id!!)

        val vector1Actualizado = vectorService.recuperarVector(vectorCreado1.id!!)
        val vector2Movido = vectorService.recuperarVector(vectorCreado2.id!!)

        Assertions.assertEquals(vector2Movido.ubicacion.id!!, vector1Actualizado.ubicacion.id!!)
        Assertions.assertFalse(vector1Actualizado.estaSano())
    }


    @Test
    fun alMoverUnVectorNoInfectadoAUnaUbicacionEntoncesNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMover")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMover2")
        val vectorNoInfectado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorNoInfectado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        Assertions.assertTrue(vectorNoInfectado2.estaSano())
        Assertions.assertTrue(vectorNoInfectado1.estaSano())
        Assertions.assertTrue(vectorNoInfectado2.tipo.puedeSerInfectado(vectorNoInfectado1.tipo))

        ubicacionService.mover(vectorNoInfectado1.id!!, ubicacionCreada1.id!!)

        val vectorNoInfectado2Actualizado = vectorService.recuperarVector(vectorNoInfectado2.id!!)
        val vectorNoInfectado1Actualizado = vectorService.recuperarVector(vectorNoInfectado1.id!!)

        Assertions.assertEquals(vectorNoInfectado1Actualizado.ubicacion.id!!, vectorNoInfectado2Actualizado.ubicacion.id!!)
        Assertions.assertTrue(vectorNoInfectado2Actualizado.estaSano())
        Assertions.assertTrue(vectorNoInfectado1Actualizado.estaSano())
    }


    @Test
    fun seRecuperanTodosLosVectoresDeLaUbicacionCorrectamente() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testRecuperar")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        val vectoresEnUbicacion = ubicacionService.recuperarVectores(ubicacionCreada.id!!)

        Assertions.assertEquals(2, vectoresEnUbicacion.size)
        Assertions.assertNotNull(vectoresEnUbicacion.find {
            it.id == vectorCreado1.id  && it.ubicacion.id!! == vectorCreado1.ubicacion.id!! && it.especies.size == vectorCreado1.especies.size
        })
        Assertions.assertNotNull(vectoresEnUbicacion.find {
            it.id == vectorCreado2.id  && it.ubicacion.id!! == vectorCreado2.ubicacion.id!! && it.especies.size == vectorCreado2.especies.size
        })
    }


    @Test
    fun noSePuedeExpandirEnUnaUbicacionInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            ubicacionService.expandir(-2222)
        }
    }


    @Test
    fun alIntentarExpandirEnUnaUbicacionConUnVectorContagiadoYOtrosSanosQuePuedenSerInfectadosSeContagian() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testExpandir")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada2.id!!)

        vectorService.infectar(vectorCreado3, especieCreada)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertTrue(vectorCreado2.estaSano())
        Assertions.assertFalse(vectorCreado3.estaSano())
        Assertions.assertEquals(3, ubicacionService.recuperarVectores(ubicacionCreada.id!!).size)

        ubicacionService.expandir(ubicacionCreada.id!!)

        val vector1Actualizado = vectorService.recuperarVector(vectorCreado1.id!!)
        val vector2Actualizado = vectorService.recuperarVector(vectorCreado2.id!!)
        val vector3Actualizado = vectorService.recuperarVector(vectorCreado3.id!!)

        Assertions.assertFalse(vector1Actualizado.estaSano())
        Assertions.assertFalse(vector2Actualizado.estaSano())
        Assertions.assertFalse(vector3Actualizado.estaSano())
    }


    @Test
    fun alIntentarExpandirEnUnaUbicacionConVectoresSanosEntoncesNoHaceNada() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testExpandir")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertTrue(vectorCreado2.estaSano())
        Assertions.assertEquals(2, ubicacionService.recuperarVectores(ubicacionCreada.id!!).size)

        ubicacionService.expandir(ubicacionCreada.id!!)

        val vector1Actualizado = vectorService.recuperarVector(vectorCreado1.id!!)
        val vector2Actualizado = vectorService.recuperarVector(vectorCreado2.id!!)

        Assertions.assertTrue(vector1Actualizado.estaSano())
        Assertions.assertTrue(vector2Actualizado.estaSano())

    }

    @Test
    fun alIntentarExpandirEnUnaUbicacionSinVectoresEntoncesNoHaceNada() {
        val nuevaUbicacion = ubicacionService.crearUbicacion("expandirTest")

        Assertions.assertTrue(ubicacionService.recuperarVectores(nuevaUbicacion.id!!).isEmpty())

        ubicacionService.expandir(nuevaUbicacion.id!!)

        Assertions.assertTrue(ubicacionService.recuperarVectores(nuevaUbicacion.id!!).isEmpty())
    }

    @Test
    fun seRecuperanTodasLasUbicacionesDeManeraCorrectaConPaginaUnoYSizeDos() {
        dataService.eliminarTodo()

        ubicacionService.crearUbicacion("nombreCualquiera1")
        ubicacionService.crearUbicacion("nombreCualquiera2")
        ubicacionService.crearUbicacion("nombreCualquiera3")

        val pageable = PageRequest.of(1, 2)

        val ubicacionesRecuperadas = ubicacionService.recuperarTodos(pageable)


        Assertions.assertEquals(1, ubicacionesRecuperadas.number)
        Assertions.assertEquals(1, ubicacionesRecuperadas.numberOfElements)
    }

    // ------------------------ Neo4jTests ------------------------ //

    @Test
    fun seRecuperaUnaUbicacionDeNeo4jCorrectamente() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testNeo")
        val ubicacionRecuperadaOptional = neo4jUbicacionDAO.findByIdRelacional(ubicacionCreada.id!!)

        // Hay un error al recuperar porque crearUbicacion retorna una ubicacion con el ID de Hibernate
        // y se intenta recuperar la ubicacion en neo4J con el ID de Hibernate (no es el mismo)
        Assertions.assertTrue(ubicacionRecuperadaOptional.isPresent)

        val ubicacionRecuperada = ubicacionRecuperadaOptional.get()

        Assertions.assertEquals(ubicacionRecuperada.nombre, ubicacionCreada.nombre)
    }


    @Test
    fun hayConexionDirectaTrue() {
        dataService.eliminarTodo()
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion2")

        val ubicacionNeo1 = neo4jUbicacionDAO.findByIdRelacional(ubicacion1.id!!).get()
        val ubicacionNeo2 = neo4jUbicacionDAO.findByIdRelacional(ubicacion2.id!!).get()

        ubicacionService.conectarConQuery(ubicacionNeo1.nombre,ubicacionNeo2.nombre,"Terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacionNeo1.nombre,ubicacionNeo2.nombre))
    }
    @Test
    fun hayConexionDirectaFalse() {
        dataService.eliminarTodo()
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion2")
        val ubicacion3 = ubicacionService.crearUbicacion("ubicacion3")

        val ubicacionNeo1 = neo4jUbicacionDAO.findByIdRelacional(ubicacion1.id!!).get()
        val ubicacionNeo2 = neo4jUbicacionDAO.findByIdRelacional(ubicacion2.id!!).get()
        val ubicacionNeo3 = neo4jUbicacionDAO.findByIdRelacional(ubicacion3.id!!).get()

        ubicacionService.conectarConQuery(ubicacionNeo1.nombre,ubicacionNeo2.nombre,"Terrestre")
        ubicacionService.conectarConQuery(ubicacionNeo2.nombre,ubicacionNeo3.nombre,"Terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacionNeo1.nombre,ubicacionNeo2.nombre))
        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacionNeo2.nombre,ubicacionNeo3.nombre))
        Assertions.assertFalse(ubicacionService.hayConexionDirecta(ubicacionNeo1.nombre,ubicacionNeo3.nombre))
    }

    @Test
    fun seCreaUnaRelacionEntreDosUbicaciones() {
        dataService.eliminarTodo()
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion2")

        val ubicacionNeo1 = neo4jUbicacionDAO.findByIdRelacional(ubicacion1.id!!).get()
        val ubicacionNeo2 = neo4jUbicacionDAO.findByIdRelacional(ubicacion2.id!!).get()

        Assertions.assertFalse(ubicacionService.hayConexionDirecta(ubicacionNeo1.nombre,ubicacionNeo2.nombre))

        ubicacionService.conectarConQuery(ubicacionNeo1.nombre,ubicacionNeo2.nombre,"Terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacionNeo1.nombre,ubicacionNeo2.nombre))
    }

    @Test
    fun conectados() {
        dataService.eliminarTodo()
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion2")
        val ubicacion3 = ubicacionService.crearUbicacion("ubicacion3")
        val ubicacion4 = ubicacionService.crearUbicacion("ubicacion4")
        val ubicacion5 = ubicacionService.crearUbicacion("ubicacion5")
        val ubicacion6 = ubicacionService.crearUbicacion("ubicacion6")

        val ubicacionNeo1 = neo4jUbicacionDAO.findByIdRelacional(ubicacion1.id!!).get()
        val ubicacionNeo2 = neo4jUbicacionDAO.findByIdRelacional(ubicacion2.id!!).get()
        val ubicacionNeo3 = neo4jUbicacionDAO.findByIdRelacional(ubicacion3.id!!).get()
        val ubicacionNeo4 = neo4jUbicacionDAO.findByIdRelacional(ubicacion4.id!!).get()
        val ubicacionNeo5 = neo4jUbicacionDAO.findByIdRelacional(ubicacion5.id!!).get()
        val ubicacionNeo6 = neo4jUbicacionDAO.findByIdRelacional(ubicacion6.id!!).get()

        ubicacionService.conectarConQuery(ubicacionNeo1.nombre,ubicacionNeo3.nombre,"Terrestre")
        ubicacionService.conectarConQuery(ubicacionNeo1.nombre,ubicacionNeo5.nombre,"Terrestre")
        ubicacionService.conectarConQuery(ubicacionNeo1.nombre,ubicacionNeo6.nombre,"Terrestre")
        ubicacionService.conectarConQuery(ubicacionNeo6.nombre,ubicacionNeo2.nombre,"Terrestre")
        ubicacionService.conectarConQuery(ubicacionNeo6.nombre,ubicacionNeo4.nombre,"Terrestre")

        val conectadosConUbicacion1 = ubicacionService.conectados("ubicacion1").map{u->u.nombre}.toList()
        val conectadosConUbicacion6 = ubicacionService.conectados("ubicacion6").map{u->u.nombre}.toList()

        Assertions.assertTrue(conectadosConUbicacion1.containsAll(
            setOf(ubicacionNeo3,ubicacionNeo5,ubicacionNeo6).map{u->u.nombre}.toList()))
        Assertions.assertTrue(conectadosConUbicacion6.containsAll(setOf(
            ubicacionNeo1,ubicacionNeo2,ubicacionNeo4).map{u->u.nombre}.toList()))
        Assertions.assertFalse(conectadosConUbicacion1.containsAll(
            setOf(ubicacionNeo2,ubicacionNeo4).map{u->u.nombre}.toList()))
        Assertions.assertFalse(conectadosConUbicacion6.containsAll(
            setOf(ubicacionNeo3,ubicacionNeo5).map{u->u.nombre}.toList()))
    }
    //@AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }

}
