package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionServiceImplTest {

    val dataService = DataServiceHibernate()

    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl

    @Autowired
    private lateinit var especieService: EspecieServiceImpl

    @Autowired
    private lateinit var vectorService: VectorServiceImpl

    @BeforeEach
    fun setUp() {
       dataService.crearSetDeDatosIniciales()
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
            ubicacionService.recuperar(777)
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
        val ubicacionCreada = ubicacionService.crearUbicacion("testVectores")

        Assertions.assertTrue(ubicacionService.recuperarVectores(ubicacionCreada.id!!).isEmpty())

        ubicacionService.mover(1, ubicacionCreada.id!!)

        Assertions.assertTrue(ubicacionService.recuperarVectores(ubicacionCreada.id!!).size == 1)
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
        val vectorExistente = vectorService.recuperarVector(1)
        Assertions.assertThrows(NullPointerException::class.java) {
            ubicacionService.mover(vectorExistente.id!!, 777)
        }
    }

    @Test
    fun noSePuedeMoverUnVectorInexistenteAUnaUbicacion() {
        val ubicacionExistente = ubicacionService.recuperar(2)
        Assertions.assertThrows(NoExisteElid::class.java) {
            ubicacionService.mover(204, ubicacionExistente.id!!)
        }
    }

    @Test
    fun siUnVectorYaSeEncuentraEnUnaUbicacionEntoncesAlMoverNoSeHaceNada() {
        val ubicacionCreada = ubicacionService.crearUbicacion("nombre1")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieRecuperada = especieService.recuperarEspecie(1)

        vectorService.infectar(vectorCreado2, especieRecuperada)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertFalse(vectorCreado2.estaSano())
        Assertions.assertEquals(ubicacionService.recuperarVectores(ubicacionCreada.id!!).size, 2)

        ubicacionService.mover(vectorCreado2.id!!, ubicacionCreada.id!!)

        val vectorActualizado1 = vectorService.recuperarVector(vectorCreado1.id!!)
        val vectorActualizado2 = vectorService.recuperarVector(vectorCreado2.id!!)

        Assertions.assertTrue(vectorActualizado1.estaSano())
        Assertions.assertEquals(vectorCreado2.especies.size, vectorActualizado2.especies.size)
        Assertions.assertEquals(ubicacionService.recuperarVectores(ubicacionCreada.id!!).size, 2)
    }

    @Test
    fun seMueveUnVectorAUnaUbicacionCorrectamente() {
        val ubicacionRecuperada = ubicacionService.recuperar(1)
        val ubicacionCreada = ubicacionService.crearUbicacion("testMover")
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionRecuperada.id!!)

        Assertions.assertTrue(vectorCreado.ubicacion.id != ubicacionCreada.id)

        ubicacionService.mover(vectorCreado.id!!, ubicacionCreada.id!!)

        val vectorActualizado = vectorService.recuperarVector(vectorCreado.id!!)

        Assertions.assertEquals(vectorActualizado.ubicacion.id, ubicacionCreada.id)
        Assertions.assertEquals(vectorActualizado.ubicacion.nombre, ubicacionCreada.nombre)
    }

    @Test
    fun alMoverUnVectorInfectadoAUnaUbicacionEntoncesSeInfectaUnVectorAlAzar() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testMoverInfectar")
        val ubicacionRecuperada = ubicacionService.recuperar(1)
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionRecuperada.id!!)
        val especieRecuperada = especieService.recuperarEspecie(1)

        vectorService.infectar(vectorCreado2, especieRecuperada)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertFalse(vectorCreado2.estaSano())

        ubicacionService.mover(vectorCreado2.id!!, ubicacionCreada.id!!)

        val vector1Actualizado = vectorService.recuperarVector(vectorCreado1.id!!)
        val vector2Movido = vectorService.recuperarVector(vectorCreado2.id!!)

        Assertions.assertEquals(vector2Movido.ubicacion.id!!, vector1Actualizado.ubicacion.id!!)
        Assertions.assertFalse(vector1Actualizado.estaSano())
    }

    @Test
    fun alMoverUnVectorNoInfectadoAUnaUbicacionEntoncesNoSeHaceNada() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testMover")
        val ubicacionRecuperada = ubicacionService.recuperar(2)
        val vectorNoInfectado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionRecuperada.id!!)
        val vectorNoInfectado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertTrue(vectorNoInfectado2.estaSano())
        Assertions.assertTrue(vectorNoInfectado1.estaSano())
        Assertions.assertTrue(vectorNoInfectado2.tipo.puedeSerInfectado(vectorNoInfectado1.tipo))

        ubicacionService.mover(vectorNoInfectado1.id!!, ubicacionCreada.id!!)

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
            ubicacionService.expandir(2222)
        }
    }

    @Test
    fun alIntentarExpandirEnUnaUbicacionConUnVectorContagiadoYOtrosSanosQuePuedenSerInfectadosSeContagian() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testExpandir")
        val vectorCreado1 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especie = especieService.recuperarEspecie(1)

        vectorService.infectar(vectorCreado3, especie)

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

    @AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }
}