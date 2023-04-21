package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Diosito
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
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

    val especieDAO = HibernateEspecieDAO()
    val especieService = EspecieServiceImpl(especieDAO)

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
        Assertions.assertEquals(vectorActualizado.especies.size, 4)
    }

    @Test
    fun alMoverUnVectorInfectadoAUnaUbicacionEntoncesSeInfectaUnVectorAlAzar() {
        val ubicacion = ubicacionService.recuperar(8)
        val vectorInfectado = vectorService.recuperarVector(8)
        val vectorNoInfectado = vectorService.recuperarVector(7)

        Assertions.assertTrue(vectorNoInfectado.estaSano())

        ubicacionService.mover(vectorInfectado.id!!, ubicacion.id!!)

        val vectorInfectadoYLoSabe = vectorService.recuperarVector(vectorNoInfectado.id!!)
        val vectorInfectadoMovido = vectorService.recuperarVector(vectorInfectado.id!!)

        Assertions.assertEquals(vectorInfectadoMovido.ubicacion.id!!, vectorInfectadoYLoSabe.ubicacion.id!!)
        Assertions.assertFalse(vectorInfectadoYLoSabe.estaSano())
        //forEach
    }

    @Test
    fun alMoverUnVectorNoInfectadoAUnaUbicacionEntoncesNoSeHaceNada() {

    }










    @AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }
}