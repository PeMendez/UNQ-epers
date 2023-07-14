package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfeccionReporteTest {

    @Autowired
    private lateinit var infeccionSegunPatogenoService: InfeccionSegunPatogenoServiceImpl
    @Autowired
    private lateinit var infeccionReporteService: InfeccionReporteServiceImpl
    @Autowired
    private lateinit var infeccionSegunEspecieService: InfeccionSegunEspecieServiceImpl
    @Autowired
    private lateinit var ubicacionService: UbicacionServiceImpl
    @Autowired
    private lateinit var vectorService: VectorServiceImpl
    @Autowired
    private lateinit var especieService: EspecieServiceImpl
    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl
    @Autowired
    private lateinit var dataService : DataService

    @Test
    fun seCreaUnReporteDeInfeccionSegunPatogenoCorrectamente() {
        val reporte = infeccionSegunPatogenoService.agregarReporteDeInfeccion(1, 2,3,"tipo1", TipoDeVector.Persona)

        Assertions.assertNotNull(reporte.id)
    }

    @Test
    fun seCreaUnReporteDeInfeccionRelacionalCorrectamente() {
        val reporte = infeccionReporteService.agregarInfeccionReporte(1, 1)

        Assertions.assertNotNull(reporte.id!!)
    }

    @Test
    fun seCreaUnReporteDeInfeccionSegunEspecieCorrectamente() {
        val reporte = infeccionSegunEspecieService.agregarReporteDeInfeccion(1, "especie1", "Arg", TipoDeVector.Persona,)

        Assertions.assertNotNull(reporte.id)
    }

    @Test
    fun alInfectarUnVectorSeGeneraUnReporteRelacionalCorrectamente() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testReporte", GeoJsonPoint(1.0, 1.0))
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testReporte2", GeoJsonPoint(2.0, 2.0))
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)

        val patogeno = Patogeno("tipo1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "nuevaEspecie", ubicacionCreada2.id!!)

        vectorService.infectar(vectorCreado, especieCreada)

        val reportes = infeccionReporteService.findAllByVectorId(vectorCreado.id!!)

        Assertions.assertNotNull(reportes.find { it.idVectorInfectado == vectorCreado.id!! })
        Assertions.assertNotNull(reportes.find { it.idEspecie == especieCreada.id!! })
        Assertions.assertTrue(reportes.size == 1)
    }

    @Test
    fun alInfectarUnVectorSeGeneraUnReporteDeInfeccionSegunPatogenoCorrectamente() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testReporte", GeoJsonPoint(1.0, 1.0))
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testReporte2", GeoJsonPoint(2.0, 2.0))
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)

        val patogeno = Patogeno("tipo1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "nuevaEspecie", ubicacionCreada2.id!!)

        vectorService.infectar(vectorCreado, especieCreada)

        val reportes = infeccionSegunPatogenoService.findAll()

        Assertions.assertNotNull(reportes.find { it.idVectorInfectado == vectorCreado.id!! })
        Assertions.assertNotNull(reportes.find { it.tipoDePatogeno == patogenoCreado.tipo })
        Assertions.assertNotNull(reportes.find { it.capacidadDeBiomecanizacionPatogeno == patogenoCreado.capacidadDeBiomecanizacion })
        Assertions.assertNotNull(reportes.find { it.capacidadDeContagioPatogeno == patogenoCreado.capacidadDeContagio })
        Assertions.assertNotNull(reportes.find { it.tipoDeVectorInfectado == vectorCreado.tipo})
        Assertions.assertTrue(reportes.size == 1)
    }

    @Test
    fun nuevo() {
        val ubicacionCreada = ubicacionService.crearUbicacion("testReporte1", GeoJsonPoint(1.0, 1.0))
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testReporte22", GeoJsonPoint(2.0, 2.0))
        val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)

        val patogeno = Patogeno("tipo1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "nuevaEspecie", ubicacionCreada2.id!!)

        vectorService.infectar(vectorCreado, especieCreada)

        val reportes = infeccionSegunEspecieService.findAll()

        Assertions.assertNotNull(reportes.find { it.idVectorInfectado == vectorCreado.id!! })
        Assertions.assertNotNull(reportes.find { it.tipoDeVectorInfectado == vectorCreado.tipo})
        Assertions.assertTrue(reportes.size == 1)
    }


    @Test
    fun crearReportes() {
        val ubicacionCreada2 = ubicacionService.crearUbicacion("testReporte22", GeoJsonPoint(2.0, 2.0))
        vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val patogeno = Patogeno("tipo1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "nuevaEspecie1", ubicacionCreada2.id!!)
        val ubicacionCreada = ubicacionService.crearUbicacion("testReporte1", GeoJsonPoint(1.0, 1.0))

        var i = 0
        while (i < 100000) {
            val vectorCreado = vectorService.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
            vectorService.infectar(vectorCreado, especieCreada)

            i += 1
        }
    }

    @Test
    fun buscarDatosDePatogenoDeManeraRelacional() {
        val begin = System.currentTimeMillis()

        //infeccionReporteService.findAll

        val end = System.currentTimeMillis()
        println("TIEMPO TRANSCURRIDO: ${end-begin}")
    }

    //@AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }
}