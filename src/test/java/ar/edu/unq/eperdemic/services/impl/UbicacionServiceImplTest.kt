package ar.edu.unq.eperdemic.services.impl

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
    fun recuperarUbicacionConTodosSusDatosCorrectos() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testRecuperar")
        val ubicacionRecuperada = ubicacionService.recuperar(ubicacionCreada.id!!)

        Assertions.assertEquals(ubicacionCreada.nombre, ubicacionRecuperada.nombre)
    }

    @Test
    fun noPuedenExistirDosUbicacionesConElMismoNombre() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("mismoNombreTest")
        try {
            val ubicacionCreada2 = ubicacionService.crearUbicacion("mismoNombreTest")
            fail("Debería haber lanzado una excepción de restricción única")
        } catch (ex: Exception) {
            Assertions.assertTrue(ex is Exception)
        }
    }

    @Test
    fun puedenExistirDosUbicacionesConNombresDistintos() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("nombreDistinto")
        try {
            val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombre")
        } catch (ex: Exception) {
            fail("No tendria que haber lanzado una excepcion porque son distintos nombres")
        }
    }

    @Test
    fun seMueveUnVectorAUnaUbicacionCorrectamente() {
        val ubicacion = ubicacionService.recuperar(1)
        val vector = vectorService.recuperarVector(3)

        Assertions.assertTrue(vector.ubicacion.nombre != ubicacion.nombre)

        ubicacionService.mover(vector.id!!, ubicacion.id!!)

        Assertions.assertTrue(vector.ubicacion.nombre == ubicacion.nombre)

    }

    @AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }
}