package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.modelo.TipoDeMutacion
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MutacionServiceImplTest {

    @Autowired
    private lateinit var mutacionService: MutacionServiceImpl
    @Autowired
    private lateinit var dataService: DataService
    private var mutacionTest = Mutacion()

    @BeforeEach
    fun setUp() {
        Random.switchModo(false)
        dataService.crearSetDeDatosIniciales()
        mutacionTest.tipoDeMutacion = TipoDeMutacion.BioalteracionGenetica // se deberia inicializar mutacion con un tipo
    }

    @Test
    fun SeAgregaUnaMutacionAUnaEspecie() {
    }
    @Test
    fun seIntentaAgregarUnaMutacionAUnaEspecieInexistenteYRompe() {
        assertThrows(NoExisteElid::class.java) {
            mutacionService.agregarMutacion(-11, mutacionTest)
        }
    }

    @AfterEach
    fun eliminarModelo() {
        dataService.eliminarTodo()
    }
}