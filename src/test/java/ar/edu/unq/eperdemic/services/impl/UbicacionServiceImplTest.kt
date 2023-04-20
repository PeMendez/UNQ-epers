package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Diosito
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeUbicacionRepetido
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.utils.DataServiceHibernate
import org.junit.Assert
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
        val ubicacionCreada = ubicacionService.crearUbicacion("testRecuperar")
        val ubicacionRecuperada = ubicacionService.recuperar(ubicacionCreada.id!!)

        Assertions.assertTrue(ubicacionCreada.vectores.isEmpty())
        Assertions.assertTrue(ubicacionRecuperada.vectores.isEmpty())
        Assertions.assertEquals(ubicacionCreada.nombre, ubicacionRecuperada.nombre)
    }

    @Test
    fun noPuedenExistirDosUbicacionesConElMismoNombre() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("mismoNombreTest")
        try {
            val ubicacionCreada2 = ubicacionService.crearUbicacion("mismoNombreTest")
            fail("Debería haber lanzado una excepción de restricción única")
        } catch (ex: Exception) {
            Assertions.assertTrue(ex is NombreDeUbicacionRepetido)
        }
    }

    @Test
    fun puedenExistirDosUbicacionesConNombresDistintos() {
        val ubicacionCreada1 = ubicacionService.crearUbicacion("nombreDistinto")
        try {
            val ubicacionCreada2 = ubicacionService.crearUbicacion("otroNombre")
        } catch (ex: NombreDeUbicacionRepetido) {
            fail("No tendria que haber lanzado una excepcion porque son distintos nombres")
        }
    }

    @Test
    fun seRecuperanTodasLasUbicacionesDeManeraCorrecta() {
        val ubicacion1 = ubicacionService.recuperar(1)
        val ubicacion2 = ubicacionService.recuperar(2)
        val ubicacion3 = ubicacionService.recuperar(3)
        val listaDeUbicacionesEsperada = listOf<Ubicacion>(ubicacion1, ubicacion2, ubicacion3)

        val ubicacionesRecuperadas = ubicacionService.recuperarTodos()

        Assertions.assertEquals(ubicacionesRecuperadas.size, listaDeUbicacionesEsperada.size)

        println(ubicacionesRecuperadas[0].id!!)
        println(ubicacionesRecuperadas[1].id!!)
        println(ubicacionesRecuperadas[2].id!!)
        println(ubicacionesRecuperadas[0].nombre)
        println(ubicacionesRecuperadas[1].nombre)
        println(ubicacionesRecuperadas[2].nombre)
        println(ubicacionesRecuperadas[0].vectores.size)
        println(ubicacionesRecuperadas[1].vectores.size)
        println(ubicacionesRecuperadas[2].vectores.size)
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        println(listaDeUbicacionesEsperada[0].id!!)
        println(listaDeUbicacionesEsperada[1].id!!)
        println(listaDeUbicacionesEsperada[2].id!!)
        println(listaDeUbicacionesEsperada[0].nombre)
        println(listaDeUbicacionesEsperada[1].nombre)
        println(listaDeUbicacionesEsperada[2].nombre)
        println(listaDeUbicacionesEsperada[0].vectores.size)
        println(listaDeUbicacionesEsperada[1].vectores.size)
        println(listaDeUbicacionesEsperada[2].vectores.size)

        ubicacionesRecuperadas.forEach { ur ->
            Assertions.assertTrue(
                listaDeUbicacionesEsperada.any { ue ->
                    ue.id!! == ur.id!! && ue.nombre == ur.nombre && ue.vectores.size == ur.vectores.size
                }
            )
        }
    }

    @Test
    fun unaUbicacionRecibeAUnVectorAlMoverse() {
        val ubicacion = ubicacionService.recuperar(1)
        val vector = vectorService.recuperarVector(3)

        Assertions.assertFalse(ubicacion.vectores.any { v -> v.id!! == vector.id!! })

        ubicacionService.mover(vector.id!!, ubicacion.id!!)

        val ubicacionActualizada = ubicacionService.recuperar(ubicacion.id!!)

        Assertions.assertTrue(ubicacionActualizada.vectores.any { v -> v.id!! == vector.id!! })
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
        Diosito.switchModo(false)

        val ubicacionNueva = ubicacionService.crearUbicacion("testMoverEInfectar")

        Assertions.assertTrue(ubicacionNueva.vectores.isEmpty())

        val vectorNuevo = vectorService.crearVector(TipoDeVector.Persona, ubicacionNueva.id!!)
        val ubicacionRecuperada = ubicacionService.recuperar(ubicacionNueva.id!!)

        Assertions.assertTrue(ubicacionRecuperada.vectores.size == 1)
        ubicacionRecuperada.vectores.forEach { v -> Assertions.assertTrue(v.especies.isEmpty()) }

        val especie = especieService.recuperarEspecie(1)
        val vector2 = vectorService.recuperarVector(4)

        vectorService.infectar(vector2, especie)
        val vector2Infectado = vectorService.recuperarVector(vector2.id!!)

        Assertions.assertTrue(vector2Infectado.especies.isNotEmpty())

        ubicacionService.mover(vector2Infectado.id!!, ubicacionRecuperada.id!!)

        val ubicacionPostInfeccion = ubicacionService.recuperar(ubicacionRecuperada.id!!)

        ubicacionPostInfeccion.vectores.forEach { v -> Assertions.assertTrue(v.especies.isNotEmpty()) }

    }

    @Test
    fun alMoverUnVectorNoInfectadoAUnaUbicacionEntoncesNoSeHaceNada() {

    }


    @AfterEach
    fun clearAll() {
        dataService.eliminarTodo()
    }
}