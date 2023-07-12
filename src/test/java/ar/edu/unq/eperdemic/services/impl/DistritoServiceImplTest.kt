package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.modelo.exceptions.CoordenadasParaUnDistritoRepetidas
import ar.edu.unq.eperdemic.modelo.exceptions.NoHayUnDistritoMasEnfermo
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeDistritoRepetido
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DistritoServiceImplTest {

    @Autowired
    private lateinit var distritoServiceImpl: DistritoServiceImpl
    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl
    @Autowired
    private lateinit var vectorService: VectorServiceImpl
    @Autowired
    private lateinit var dataService: DataService
    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl

    @BeforeEach
    fun crearModelo() {
       dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun seCreaUnDistritoDeFormaCorrecta(){
        val coordenadas = GeoJsonPolygon(listOf(Point(23.5,44.0), Point(35.5,44.0),Point(28.0,14.0)))
        val distrito = Distrito("Revenclaw", coordenadas)

        val distritoCreado = distritoServiceImpl.crear(distrito)

        Assertions.assertNotNull(distritoCreado.id)
        Assertions.assertEquals(distrito.nombre, distritoCreado.nombre)
    }

    @Test
    fun noPuedenExistirDosDistritosConElMismoNombre(){
        val coordenadas = GeoJsonPolygon(mutableListOf(Point(23.5,44.0), Point(35.5,44.0),Point(28.0,14.0)))
        val distrito = Distrito("Revenclaw", coordenadas)

        distritoServiceImpl.crear(distrito)

        Assertions.assertThrows(NombreDeDistritoRepetido::class.java){
            distritoServiceImpl.crear(distrito)
        }

    }

    @Test
    fun noPuedenExistirDosDistritosConLasMismasCoordenadas(){
        val coordenadas = GeoJsonPolygon(mutableListOf( Point(23.5,44.0), Point(35.5,44.0),Point(28.0,14.0)))
        val distrito = Distrito("Revenclaw2", coordenadas)
        val distrito2 = Distrito("Hogsmeade2", coordenadas)

        distritoServiceImpl.crear(distrito)

        Assertions.assertThrows(CoordenadasParaUnDistritoRepetidas::class.java){
            distritoServiceImpl.crear(distrito2)
        }

    }

    @Test
    fun seObtieneElDistritoMasEnfermoCorrectamente() {
        dataService.eliminarTodo()
        val coordenadas1 = GeoJsonPolygon(mutableListOf(Point(0.0,0.0), Point(0.0,1.0),Point(1.0,1.0), Point(1.0,0.0), Point(0.0,0.0)))
        val distrito1 = Distrito("unDistrito", coordenadas1)
        distritoServiceImpl.crear(distrito1)

        val coordenadas2 = GeoJsonPolygon(mutableListOf(Point(8.0,8.0), Point(8.0,9.0),Point(9.0,9.0), Point(9.0,8.0), Point(8.0,8.0)))
        val distrito2 = Distrito("OtroDistrito", coordenadas2)
        distritoServiceImpl.crear(distrito2)

        val ubicacionEnferma1 = ubicacionService.crearUbicacion("testUbiEnfermas", GeoJsonPoint(0.0, 0.1))
        val ubicacionEnferma2 = ubicacionService.crearUbicacion("testUbiEnfermas2", GeoJsonPoint(1.0, 1.0))
        val ubicacionEnferma3 = ubicacionService.crearUbicacion("testUbiEnfermas3", GeoJsonPoint(8.0, 8.0))
        vectorService.crearVector(TipoDeVector.Persona, ubicacionEnferma1.id!!, false)
        val vectorEnfermo2 = vectorService.crearVector(TipoDeVector.Persona, ubicacionEnferma2.id!!, false)
        val vectorEnfermo3 = vectorService.crearVector(TipoDeVector.Persona, ubicacionEnferma3.id!!, false)

        val patogeno = Patogeno("testEnfermas")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!,"cualquierNombre", ubicacionEnferma1.id!!)

        vectorService.infectar(vectorEnfermo2, especieCreada)
        vectorService.infectar(vectorEnfermo3, especieCreada)

        val mongoUbicacionEnferma1 = ubicacionService.recuperarUbicacionMongoPorId(ubicacionEnferma1.id!!)
        val mongoUbicacionEnferma2 = ubicacionService.recuperarUbicacionMongoPorId(ubicacionEnferma2.id!!)
        val mongoUbicacionEnferma3 = ubicacionService.recuperarUbicacionMongoPorId(ubicacionEnferma3.id!!)

        Assertions.assertEquals(distrito1.nombre, mongoUbicacionEnferma1.distrito)
        Assertions.assertEquals(distrito1.nombre, mongoUbicacionEnferma2.distrito)
        Assertions.assertEquals(distrito2.nombre, mongoUbicacionEnferma3.distrito)

        val distritoMasEnfermo = distritoServiceImpl.distritoMasEnfermo()

        Assertions.assertEquals(distrito1.nombre, distritoMasEnfermo.nombre)
    }

    @Test
    fun cuandoNoHayDistritoMasEnfermoEntoncesSeLanzaUnaExcepcion() {
        dataService.eliminarTodo()

        Assertions.assertThrows(NoHayUnDistritoMasEnfermo::class.java) {
            distritoServiceImpl.distritoMasEnfermo()
        }
    }

    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}