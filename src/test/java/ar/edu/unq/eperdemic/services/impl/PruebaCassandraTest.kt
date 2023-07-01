package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.PruebaCassandra
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PruebaCassandraTest {

    @Autowired
    private lateinit var pruebaService: PruebaServiceImpl

    @Test
    fun crearUnaPrueba() {
        val cassandra = PruebaCassandra()
        cassandra.cantidadDeInfectados = 8
        cassandra.nombre = "pepe2"
        pruebaService.crearPrueba(cassandra)
    }

    @AfterEach
    fun eliminarModelo() {
        pruebaService.eliminarTodo()
    }
}