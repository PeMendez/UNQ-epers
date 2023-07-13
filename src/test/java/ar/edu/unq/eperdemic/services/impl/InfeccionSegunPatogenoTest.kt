package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import ar.edu.unq.eperdemic.utils.DataService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfeccionSegunPatogenoTest {

    @Autowired
    private lateinit var infeccionSegunPatogenoService: InfeccionSegunPatogenoServiceImpl
    @Autowired
    private lateinit var dataService : DataService

    @Test
    fun seCreaUnReporteDeInfeccionSegunPatogenoCorrectamente() {
        val reporte = infeccionSegunPatogenoService.agregarReporteDeInfeccion(2,3,"anashe", TipoDeVector.Persona)

        Assertions.assertNotNull(reporte.id!!)
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }
}