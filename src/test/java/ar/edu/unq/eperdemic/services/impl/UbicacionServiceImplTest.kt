package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.jupiter.api.*

class UbicacionServiceImplTest {

    val dataService = DataServiceHibernate()
    val ubicacionDAO = HibernateUbicacionDAO()
    val ubicacionService = UbicacionServiceImpl(ubicacionDAO)

    val vectorDAO = HibernateVectorDAO()
    val vectorService = VectorServiceImpl(vectorDAO)

    @BeforeEach
    fun setUp() {
       dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun unaUbicacionCreadaTieneUnIdGenerado() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testUbicacion")

        Assertions.assertTrue(ubicacionCreada.id != null)
    }

    @Test
    fun seRecuperaUnaUbicacionConTodosSusDatosCorrectos() {
        val ubicacionRecuperada = ubicacionService.recuperar(1)

        Assertions.assertEquals(ubicacionRecuperada.nombre, "ubicacion1")
    }

    @Test
    fun noPuedenExistirDosUbicacionesConElMismoNombre() {
        ubicacionService.crearUbicacion("mismoNombreTest")
        try {
            ubicacionService.crearUbicacion("mismoNombreTest")
            fail("Debería haber lanzado una excepción de restricción única")
        } catch (ex: Exception) {
            Assertions.assertTrue(ex is NombreDeUbicacionRepetido)
        }
    }

    @Test
    fun puedenExistirDosUbicacionesConNombresDistintos() {
        ubicacionService.crearUbicacion("nombreDistinto")
        try {
            ubicacionService.crearUbicacion("otroNombre")
        } catch (ex: NombreDeUbicacionRepetido) {
            fail("No tendria que haber lanzado una excepcion porque son distintos nombres")
        }
    }

    @Test
    fun seRecuperanTodasLasUbicacionesDeManeraCorrecta() {

        val ubicacionesRecuperadas = ubicacionService.recuperarTodos()

        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 1.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 2.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 3.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 4.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 5.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 6.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 7.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 8.toLong() })
        Assertions.assertNotNull(ubicacionesRecuperadas.find { it.id == 9.toLong() })

        Assertions.assertTrue(ubicacionesRecuperadas.size == 9)

    }

    @Test
    fun seMueveUnVectorAUnaUbicacionCorrectamente() {
        val ubicacion = ubicacionService.recuperar(1)
        val vector = vectorService.recuperarVector(3)

        Assertions.assertTrue(vector.ubicacion.id != ubicacion.id)

        ubicacionService.mover(vector.id!!, ubicacion.id!!)

        val vectorActualizado = vectorService.recuperarVector(vector.id!!)

        Assertions.assertEquals(vectorActualizado.ubicacion.id, ubicacion.id)
        Assertions.assertEquals(vectorActualizado.ubicacion.nombre, ubicacion.nombre)
    }

    @Test
    fun alMoverUnVectorInfectadoAUnaUbicacionEntoncesSeInfectaUnVectorAlAzar() {
        val ubicacion = ubicacionService.recuperar(8)
        val vectorInfectado = vectorService.recuperarVector(8)
        val vectorNoInfectado = vectorService.recuperarVector(7)

        Assertions.assertTrue(vectorNoInfectado.estaSano())

        ubicacionService.mover(vectorInfectado.id!!, ubicacion.id!!)

        val vectorNoInfectadoActualizado = vectorService.recuperarVector(vectorNoInfectado.id!!)
        val vectorInfectadoMovido = vectorService.recuperarVector(vectorInfectado.id!!)

        Assertions.assertEquals(vectorInfectadoMovido.ubicacion.id!!, vectorNoInfectadoActualizado.ubicacion.id!!)
        Assertions.assertFalse(vectorNoInfectadoActualizado.estaSano())
    }

    @Test
    fun alMoverUnVectorNoInfectadoAUnaUbicacionEntoncesNoSeHaceNada() {
        val ubicacion = ubicacionService.recuperar(8)
        val vectorNoInfectado1 = vectorService.recuperarVector(2)
        val vectorNoInfectado2 = vectorService.recuperarVector(7)

        Assertions.assertTrue(vectorNoInfectado2.estaSano())
        Assertions.assertTrue(vectorNoInfectado1.estaSano())
        Assertions.assertTrue(vectorNoInfectado2.tipo.puedeSerInfectado(vectorNoInfectado1.tipo))

        ubicacionService.mover(vectorNoInfectado1.id!!, ubicacion.id!!)

        val vectorNoInfectado2Actualizado = vectorService.recuperarVector(vectorNoInfectado2.id!!)
        val vectorNoInfectado1Actualizado = vectorService.recuperarVector(vectorNoInfectado1.id!!)

        Assertions.assertEquals(vectorNoInfectado1Actualizado.ubicacion.id!!, vectorNoInfectado2Actualizado.ubicacion.id!!)
        Assertions.assertTrue(vectorNoInfectado2Actualizado.estaSano())
        Assertions.assertTrue(vectorNoInfectado1Actualizado.estaSano())
    }

    @Test
    fun seRecuperanTodosLosVectoresDeLaUbicacionCorrectamente() {
        val ubicacion = ubicacionService.recuperar(2)
        val vector2 = vectorService.recuperarVector(2)
        val vector8 = vectorService.recuperarVector(8)

        val vectoresEnUbicacion = ubicacionService.recuperarVectores(2)

        Assertions.assertEquals(2, vectoresEnUbicacion.size)
        Assertions.assertNotNull(vectoresEnUbicacion.find { it.id == vector2.id  && it.ubicacion.id!! == ubicacion.id!! && it.especies.size == vector2.especies.size})
        Assertions.assertNotNull(vectoresEnUbicacion.find { it.id == vector8.id  && it.ubicacion.id!! == ubicacion.id!! && it.especies.size == vector8.especies.size})
    }

    @Test
    fun alIntentarExpandirEnUnaUbicacionConUnVectorContagiadoYOtrosSanosQuePuedenSerInfectadosSeContagian() {
        val vectoresUbicacion3 = ubicacionService.recuperarVectores(3)
        val vectorSano1 = vectoresUbicacion3[0]
        val vectorSano2 = vectoresUbicacion3[1]
        val vectorInfectado = vectoresUbicacion3[2]

        Assertions.assertTrue(vectorSano1.estaSano())
        Assertions.assertTrue(vectorSano2.estaSano())
        Assertions.assertFalse(vectorInfectado.estaSano())
        Assertions.assertEquals(3, vectoresUbicacion3.size)

        ubicacionService.expandir(3)

        val vectorSano1Actualizado = vectorService.recuperarVector(vectorSano1.id!!)
        val vectorSano2Actualizado = vectorService.recuperarVector(vectorSano2.id!!)
        val vectorInfectadoActualizado = vectorService.recuperarVector(vectorInfectado.id!!)

        Assertions.assertFalse(vectorSano1Actualizado.estaSano())
        Assertions.assertFalse(vectorSano2Actualizado.estaSano())
        Assertions.assertFalse(vectorInfectadoActualizado.estaSano())
    }

    @Test
    fun alIntentarExpandirEnUnaUbicacionConVectoresSanosEntoncesNoHaceNada() {
        val vectoresUbicacion3 = ubicacionService.recuperarVectores(7)
        val vectorSano1 = vectoresUbicacion3[0]
        val vectorSano2 = vectoresUbicacion3[1]

        Assertions.assertTrue(vectorSano1.estaSano())
        Assertions.assertTrue(vectorSano2.estaSano())
        Assertions.assertEquals(2, vectoresUbicacion3.size)

        ubicacionService.expandir(7)

        val vectorSano1Actualizado = vectorService.recuperarVector(vectorSano1.id!!)
        val vectorSano2Actualizado = vectorService.recuperarVector(vectorSano2.id!!)

        Assertions.assertTrue(vectorSano1Actualizado.estaSano())
        Assertions.assertTrue(vectorSano2Actualizado.estaSano())
    }

    @AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }
}