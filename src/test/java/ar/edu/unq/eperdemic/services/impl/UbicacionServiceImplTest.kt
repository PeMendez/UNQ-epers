package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.*
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


    @BeforeEach
    fun setUp() {
        dataService.crearSetDeDatosIniciales()
        Random.switchModo(false)
    }

    @Test
    fun seGuardaUnaUbicacionCorrectamenteEnAmbasBasesDeDatos() {
        val ubicacionAGuardar = ubicacionService.crearUbicacion("testGuardarUbi")

        val ubicacionRecuperada = ubicacionService.recuperarUbicacionPorNombre(ubicacionAGuardar.nombre)
        val ubicacionNeo4j = ubicacionService.recuperarUbicacionPorNombreSiExiste(ubicacionAGuardar.nombre)

        Assertions.assertNotNull(ubicacionRecuperada.id)
        Assertions.assertNotNull(ubicacionNeo4j.idRelacional)
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
        dataService.eliminarTodo()
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testVectores1")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testVectores2")
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)

        Assertions.assertTrue(ubicacionService.recuperarVectores(ubicacionCreada1.id!!).isEmpty())

        ubicacionService.conectar(ubicacionCreada2.nombre, ubicacionCreada1.nombre, "Terrestre")
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
        val ubicacionCreada2 = ubicacionService.crearUbicacion("nombreCualquiera2")
        val ubicacionCreada3 = ubicacionService.crearUbicacion("nombreCualquiera3")

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
    fun seMueveUnVectorAUnaUbicacionCorrectamente() {
        dataService.eliminarTodo()
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMover1")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMover2")
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        Assertions.assertTrue(vectorCreado.ubicacion.id != ubicacionCreada2.id)

        ubicacionService.conectar(ubicacionCreada1.nombre, ubicacionCreada2.nombre, "Terrestre")

        ubicacionService.mover(vectorCreado.id!!, ubicacionCreada2.id!!)

        val vectorActualizado = vectorService.recuperarVector(vectorCreado.id!!)

        Assertions.assertEquals(vectorActualizado.ubicacion.id, ubicacionCreada2.id)
        Assertions.assertEquals(vectorActualizado.ubicacion.nombre, ubicacionCreada2.nombre)
    }


    @Test
    fun alMoverUnVectorInfectadoAUnaUbicacionEntoncesSeInfectaUnVectorAlAzar() {
        dataService.eliminarTodo()
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMoverInfectar1")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMoverInfectar2")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        ubicacionService.conectar(ubicacionCreada2.nombre, ubicacionCreada1.nombre, "Terrestre")

        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada3 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada3.id!!)
        val especieCreada =
            patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada3.id!!)

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
        dataService.eliminarTodo()

        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMover")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMover2")
        val vectorNoInfectado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorNoInfectado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        Assertions.assertTrue(vectorNoInfectado2.estaSano())
        Assertions.assertTrue(vectorNoInfectado1.estaSano())
        Assertions.assertTrue(vectorNoInfectado2.tipo.puedeSerInfectado(vectorNoInfectado1.tipo))

        ubicacionService.conectar(ubicacionCreada2.nombre, ubicacionCreada1.nombre, "Terrestre")
        ubicacionService.mover(vectorNoInfectado1.id!!, ubicacionCreada1.id!!)

        val vectorNoInfectado2Actualizado = vectorService.recuperarVector(vectorNoInfectado2.id!!)
        val vectorNoInfectado1Actualizado = vectorService.recuperarVector(vectorNoInfectado1.id!!)

        Assertions.assertEquals(
            vectorNoInfectado1Actualizado.ubicacion.id!!,
            vectorNoInfectado2Actualizado.ubicacion.id!!
        )
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
            it.id == vectorCreado1.id && it.ubicacion.id!! == vectorCreado1.ubicacion.id!! && it.especies.size == vectorCreado1.especies.size
        })
        Assertions.assertNotNull(vectoresEnUbicacion.find {
            it.id == vectorCreado2.id && it.ubicacion.id!! == vectorCreado2.ubicacion.id!! && it.especies.size == vectorCreado2.especies.size
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
        dataService.eliminarTodo()
        val ubicacionCreada = ubicacionService.crearUbicacion("testExpandir")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("ubicacionTestEspecie")
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreada =
            patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre", ubicacionCreada2.id!!)

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
        dataService.eliminarTodo()
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
        val ubicacionRecuperadaOptional = ubicacionService.recuperarUbicacionNeoPorId(ubicacionCreada.id!!)

        Assertions.assertEquals(ubicacionCreada.id, ubicacionRecuperadaOptional.idRelacional)
        Assertions.assertEquals(ubicacionRecuperadaOptional.nombre, ubicacionCreada.nombre)
    }

    @Test
    fun noSePuedeRecuperarUnaUbicacionDeNeo4jConUnIdInexistente() {

        Assertions.assertThrows(NoExisteElid::class.java){
            ubicacionService.recuperarUbicacionNeoPorId(-2222)
        }
    }

    @Test
    fun noSePuedeConectarDosUbicacionesConNombresInexistentes() {

        Assertions.assertThrows(NoExisteElNombreDeLaUbicacion::class.java) {
            ubicacionService.conectar("nombreNoExistente", "nombreNoExistente2", "Terrestre")
        }
    }

    @Test
    fun noSePuedeObtenerConexionDirectaDeDosUbicacionesConNombresInexistentes() {

        Assertions.assertThrows(NoExisteElNombreDeLaUbicacion::class.java) {
            ubicacionService.hayConexionDirecta("nombre1", "nombre2")
        }
    }

    @Test
    fun noSePuedenObtenerLosConectadosDeUnaUbicacionConUnNombreInexistente() {

        Assertions.assertThrows(NoExisteElNombreDeLaUbicacion::class.java) {
            ubicacionService.conectados("nombreInexistente")
        }
    }

    @Test
    fun noSePuedeConectarADosUbicacionesPorMedioDeUnCaminoInvalido() {
        val ubicacion1 = ubicacionService.crearUbicacion("testConectarFalso1")
        val ubicacion2 = ubicacionService.crearUbicacion("testConectarFalso2")

        Assertions.assertThrows(TipoDeCaminoInvalido::class.java) {
            ubicacionService.conectar(ubicacion1.nombre, ubicacion2.nombre, "123")
        }
        Assertions.assertThrows(TipoDeCaminoInvalido::class.java) {
            ubicacionService.conectar(ubicacion1.nombre, ubicacion2.nombre, "Tierra")
        }
        Assertions.assertThrows(TipoDeCaminoInvalido::class.java) {
            ubicacionService.conectar(ubicacion1.nombre, ubicacion2.nombre, "@Terrestre")
        }
        Assertions.assertThrows(TipoDeCaminoInvalido::class.java) {
            ubicacionService.conectar(ubicacion1.nombre, ubicacion2.nombre, "Maritimo1")
        }
    }

    @Test
    fun hayConexionDirectaTrue() {
        val ubicacion1 = ubicacionService.crearUbicacion("neoUbicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("neoUbicacion2")

        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"Terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacion1.nombre,ubicacion2.nombre))
    }

    @Test
    fun hayConexionDirectaFalse() {
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion123")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion234")
        val ubicacion3 = ubicacionService.crearUbicacion("ubicacion345")

        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"Terrestre")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion3.nombre,"Terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacion1.nombre,ubicacion2.nombre))
        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacion2.nombre,ubicacion3.nombre))
        Assertions.assertFalse(ubicacionService.hayConexionDirecta(ubicacion1.nombre,ubicacion3.nombre))
    }

    @Test
    fun seConectanDosUbicacionesCorrectamente() {
        val ubicacion1 = ubicacionService.crearUbicacion("nuevaUbicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("nuevaUbicacion2")

        Assertions.assertFalse(ubicacionService.hayConexionDirecta(ubicacion1.nombre,ubicacion2.nombre))

        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"Terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacion1.nombre,ubicacion2.nombre))
    }

    @Test
    fun cuandoUnaUbicacionTieneConectadosEntoncesSeRetornanCorrectamente() {
        val ubicacion1 = ubicacionService.crearUbicacion("nuevaUbicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("nuevaUbicacion2")
        val ubicacion3 = ubicacionService.crearUbicacion("nuevaUbicacion3")

        ubicacionService.conectar(ubicacion1.nombre,ubicacion3.nombre,"Terrestre")
        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"Terrestre")

        val conectadosConUbicacion1 = ubicacionService.conectados(ubicacion1.nombre)

        Assertions.assertNotNull(conectadosConUbicacion1.find { u -> u.id!! == ubicacion2.id!! })
        Assertions.assertNotNull(conectadosConUbicacion1.find { u -> u.id!! == ubicacion3.id!! })
        Assertions.assertTrue(conectadosConUbicacion1.size == 2)
    }

    @Test
    fun seRetornanUnicamenteLosConectadosAUnPasoDeDistanciaDeUnaUbicacion() {
        val ubicacion1 = ubicacionService.crearUbicacion("nuevaUbicacion1")
        val ubicacion2 = ubicacionService.crearUbicacion("nuevaUbicacion2")
        val ubicacion3 = ubicacionService.crearUbicacion("nuevaUbicacion3")

        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"Terrestre")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion3.nombre,"Terrestre")

        val conectadosConUbicacion1 = ubicacionService.conectados(ubicacion1.nombre)

        Assertions.assertNotNull(conectadosConUbicacion1.find { u -> u.id!! == ubicacion2.id!! })
        Assertions.assertTrue(conectadosConUbicacion1.size == 1)
    }

    @Test
    fun cuandoUnaUbicacionNoTieneConectadosEntoncesSeRetornaUnaListaVacia() {
        val ubicacion1 = ubicacionService.crearUbicacion("nuevaUbicacion1")


        val conectadosUbicacion1 = ubicacionService.conectados(ubicacion1.nombre)

        Assertions.assertTrue(conectadosUbicacion1.isEmpty())
    }

    @Test
    fun cuandoSeConectanDosUbicacionesEntoncesSeCreaUnCaminoUnidireccional() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMoverUbicacion")
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMoverUbicacion2")


        ubicacionService.conectar(ubicacionCreada1.nombre, ubicacionCreada2.nombre,"terrestre")

        Assertions.assertTrue(ubicacionService.hayConexionDirecta(ubicacionCreada1.nombre, ubicacionCreada2.nombre))
        Assertions.assertFalse(ubicacionService.hayConexionDirecta(ubicacionCreada2.nombre, ubicacionCreada1.nombre))
    }

    @Test
    fun unVectorInsectoNoPuedeMoverseAUnaUbicacionPorUnCaminoMaritimo() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMoverUbicacion")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMoverUbicacion2")

        ubicacionService.conectar(ubicacionCreada1.nombre, ubicacionCreada2.nombre,"Maritimo")

        Assertions.assertThrows(UbicacionNoAlcanzable ::class.java ){
            ubicacionService.mover(vectorCreado1.id!!, ubicacionCreada2.id!!)
        }
    }

    @Test
    fun unVectorPersonaNoPuedeMoverseAUnaUbicacionPorUnCaminoAereo() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("testMoverUbicacion")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testMoverUbicacion2")


        ubicacionService.conectar(ubicacionCreada1.nombre, ubicacionCreada2.nombre,"Aereo")

        Assertions.assertThrows(UbicacionNoAlcanzable ::class.java ){
            ubicacionService.mover(vectorCreado1.id!!, ubicacionCreada2.id!!)
        }
    }

    @Test
    fun noEsPosibleMoverALaMismaUbicacion() {
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion123")
        val vector = vectorService.crearVector(TipoDeVector.Animal,ubicacion1.id!!)

        Assertions.assertThrows(EsMismaUbicacion ::class.java ){
            ubicacionService.mover(vector.id!!, ubicacion1.id!!)
        }
    }

    @Test
    fun cuandoNoHayConexionEntreDosUbicacionesLaUbicacionEsMuyLejana() {
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion123")
        val vector = vectorService.crearVector(TipoDeVector.Persona,ubicacion1.id!!)
        val ubicacion3 = ubicacionService.crearUbicacion("ubicacion323")

        Assertions.assertThrows(UbicacionMuyLejana ::class.java ){
            ubicacionService.mover(vector.id!!, ubicacion3.id!!)
        }
    }


    @Test
    fun unVectorSeMuevePorElCaminoMasCortoPosibleDeFormaCorrecta(){
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion123")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion223")
        val ubicacion3 = ubicacionService.crearUbicacion("ubicacion323")
        val ubicacion4 = ubicacionService.crearUbicacion("ubicacion423")
        val ubicacion5 = ubicacionService.crearUbicacion("ubicacion523")
        val ubicacion6 = ubicacionService.crearUbicacion("ubicacion623")

        val vector = vectorService.crearVector(TipoDeVector.Animal,ubicacion1.id!!)


        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion6.nombre,"MARITIMO")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion3.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion3.nombre,ubicacion4.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion4.nombre,ubicacion5.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion5.nombre,ubicacion6.nombre,"TERRESTRE")

        ubicacionService.moverMasCorto(vector.id!!, "ubicacion623")

        val vectorM = vectorService.recuperarVector(vector.id!!)

        Assertions.assertTrue(vectorM.ubicacion.nombre == "ubicacion623")
    }

    @Test
    fun unVectorSeMuevePorElCaminoMasCortoPosibleDeFormaCorrectaYMientrasSeMueveContagiaEnTodasLasUbicacionesPorLasQuePasa(){
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion123")
        val ubicacion2 = ubicacionService.crearUbicacion("ubicacion223")
        val ubicacion3 = ubicacionService.crearUbicacion("ubicacion323")
        val ubicacion4 = ubicacionService.crearUbicacion("ubicacion423")
        val ubicacion5 = ubicacionService.crearUbicacion("ubicacion523")
        val ubicacion6 = ubicacionService.crearUbicacion("ubicacion623")

        val crookshanks = vectorService.crearVector(TipoDeVector.Animal,ubicacion1.id!!)
        val scabbers = vectorService.crearVector(TipoDeVector.Insecto, ubicacion2.id!!)
        val hedwig = vectorService.crearVector(TipoDeVector.Insecto, ubicacion6.id!!)
        val patogeno = Patogeno("Cruciartus")
        val patogenoP = patogenoService.crearPatogeno(patogeno)
        patogenoService.agregarEspecie(patogenoP.id!!, "Imperius", ubicacion1.id!!)

        val crookshanksE = vectorService.recuperarVector(crookshanks.id!!)

        Assertions.assertFalse(crookshanksE.estaSano())
        Assertions.assertTrue(scabbers.estaSano())
        Assertions.assertTrue(hedwig.estaSano())


        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion6.nombre,"MARITIMO")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion3.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion3.nombre,ubicacion4.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion4.nombre,ubicacion5.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion5.nombre,ubicacion6.nombre,"TERRESTRE")

        ubicacionService.moverMasCorto(crookshanks.id!!, "ubicacion623")

        val crookshanksM = vectorService.recuperarVector(crookshanks.id!!)
        val scabbersE = vectorService.recuperarVector(scabbers.id!!)
        val hedwigE = vectorService.recuperarVector(hedwig.id!!)

        Assertions.assertFalse(scabbersE.estaSano())
        Assertions.assertTrue(crookshanksM.nombreDeUbicacionActual() == "ubicacion623")
        Assertions.assertFalse(hedwigE.estaSano())
    }

    @Test
    fun noEsPosibleCaminoMasCortoALaMismaUbicacion() {
        val ubicacion1 = ubicacionService.crearUbicacion("ubicacion123")
        val vector = vectorService.crearVector(TipoDeVector.Animal,ubicacion1.id!!)

        Assertions.assertThrows(EsMismaUbicacion ::class.java ){
            ubicacionService.moverMasCorto(vector.id!!, ubicacion1.nombre)
        }
    }

    @Test
    fun unVectorNoPuedeMoversePorElCaminoMasCortoSiNoExisteUnaConexionEntreLasUbicacionesDadas(){
        val ubicacion1 = ubicacionService.crearUbicacion("Hogsmade")
        ubicacionService.crearUbicacion("PrivetDrive")


        val crookshanks = vectorService.crearVector(TipoDeVector.Animal,ubicacion1.id!!)

        Assertions.assertThrows(UbicacionNoAlcanzable ::class.java ){
            ubicacionService.moverMasCorto(crookshanks.id!!, "PrivetDrive")
        }

    }

    @Test
    fun unVectorNoPuedeMoversePorElCaminoMasCortoSiLaConexionEntreCaminosNoEsCompatibleConElTipoDeVector(){

        val ubicacion1 = ubicacionService.crearUbicacion("CallejonDiagon")
        val ubicacion2 = ubicacionService.crearUbicacion("Durmstrang")
        val ubicacion3 = ubicacionService.crearUbicacion("Beauxbatons")
        val ubicacion4 = ubicacionService.crearUbicacion("Hogwarts")
        val ubicacion5 = ubicacionService.crearUbicacion("Hogsmade")
        val ubicacion6 = ubicacionService.crearUbicacion("PrivetDrive")

        val ron = vectorService.crearVector(TipoDeVector.Persona,ubicacion1.id!!)


        ubicacionService.conectar(ubicacion1.nombre,ubicacion2.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion6.nombre,"MARITIMO")
        ubicacionService.conectar(ubicacion2.nombre,ubicacion3.nombre,"TERRESTRE")
        ubicacionService.conectar(ubicacion3.nombre,ubicacion4.nombre,"AEREO")
        ubicacionService.conectar(ubicacion4.nombre,ubicacion5.nombre,"AEREO")
        ubicacionService.conectar(ubicacion5.nombre,ubicacion6.nombre,"AEREO")

        Assertions.assertThrows(UbicacionNoAlcanzable ::class.java ){
            ubicacionService.moverMasCorto(ron.id!!, "Hogwarts")
        }

    }


    @AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }

}
