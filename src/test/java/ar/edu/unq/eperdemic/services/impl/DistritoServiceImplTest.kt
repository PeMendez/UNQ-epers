package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.modelo.exceptions.CoordenadasParaUnDistritoRepetidas
import ar.edu.unq.eperdemic.modelo.exceptions.NombreDeDistritoRepetido
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DistritoServiceImplTest {

    @Autowired
    private lateinit var distritoServiceImpl: DistritoServiceImpl
    @Autowired
    private lateinit var dataService: DataService

    @BeforeEach
    fun crearModelo() {
        dataService.crearSetDeDatosIniciales()
    }

    @Test
    fun seCreaUnDistritoDeFormaCorrecta(){
        val coordenadas = listOf(GeoJsonPoint(23.5,44.0), GeoJsonPoint(35.5,44.0),GeoJsonPoint(28.0,14.0))
        val distrito = Distrito("Revenclaw", coordenadas)

        val distritoCreado = distritoServiceImpl.crear(distrito)

        Assertions.assertNotNull(distritoCreado.id)
    }

    @Test
    fun noPuedenExistirDosDistritosConElMismoNombre(){
        val coordenadas = listOf(GeoJsonPoint(23.5,44.0), GeoJsonPoint(35.5,44.0),GeoJsonPoint(28.0,14.0))
        val distrito = Distrito("Revenclaw", coordenadas)

        distritoServiceImpl.crear(distrito)

        Assertions.assertThrows(NombreDeDistritoRepetido::class.java){
            distritoServiceImpl.crear(distrito)
        }

    }

    @Test
    fun noPuedenExistirDosDistritosConLasMismasCoordenadas(){
        val coordenadas = listOf(GeoJsonPoint(23.5,44.0), GeoJsonPoint(35.5,44.0),GeoJsonPoint(28.0,14.0))
        val distrito = Distrito("Revenclaw", coordenadas)
        val distrito2 = Distrito("Hogsmeade", coordenadas)

        distritoServiceImpl.crear(distrito)

        Assertions.assertThrows(CoordenadasParaUnDistritoRepetidas::class.java){
            distritoServiceImpl.crear(distrito2)
        }

    }



    @Test
    fun seObtieneElDistritoConMasUbicacionesInfectadas(){

    }

    @Test
    fun siNoHayUbicacionesInfectadasAlQuererObtenerElDistritoMasEnfermoLanzaUnaExcepcion(){
        
    }

    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}