package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import org.junit.jupiter.api.Test
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
/*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
*/
import ar.edu.unq.eperdemic.utils.DataServiceSpring
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class VectorServiceImplTest {
    @Autowired
    private lateinit var ubicacionServiceImpl: UbicacionServiceImpl
    @Autowired
    private lateinit var vectorServiceImpl: VectorServiceImpl
    @Autowired
    private lateinit var especieServiceImpl: EspecieServiceImpl
    @Autowired
    private lateinit var dataServiceSpring: DataServiceSpring
    @Autowired
    private lateinit var patogenoService: PatogenoServiceImpl

    lateinit var ubicacion1: Ubicacion
    lateinit var ubicacion2: Ubicacion
    lateinit var ubicacion3: Ubicacion
    lateinit var ubicacion4: Ubicacion
    lateinit var vectorCarnada: Vector
    lateinit var vectorPersona1: Vector
    lateinit var vectorPersona2: Vector
    lateinit var vectorAnimal1: Vector
    lateinit var vectorAnimal2: Vector
    lateinit var vectorInsecto1: Vector
    lateinit var vectorInsecto2: Vector
    lateinit var patogeno1: Patogeno
    lateinit var patogeno2: Patogeno
    lateinit var especie1: Especie
    lateinit var especie2: Especie
    lateinit var especie3: Especie
//    lateinit var especie4: Especie

    @BeforeEach
    fun setUp() {

        Random.switchModo(false)

        dataServiceSpring.eliminarTodo()

        //ubicaciones
        ubicacion1 = ubicacionServiceImpl.crearUbicacion("ubicacion1")
        ubicacion2 = ubicacionServiceImpl.crearUbicacion("ubicacion2")

        //patogeno
        patogeno1 = patogenoService.crearPatogeno(Patogeno("patogeno1"))

        //especies
        vectorCarnada = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion1.id!!)
        especie1 = patogenoService.agregarEspecie(patogeno1.id!!,"especie1",ubicacion1.id!!)
        especie2 = patogenoService.agregarEspecie(patogeno1.id!!,"especie2",ubicacion1.id!!)
        especie3 = patogenoService.agregarEspecie(patogeno1.id!!,"especie3",ubicacion1.id!!)

        //vectores sanos de cada tipo
        //persona
        vectorPersona1 = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion1.id!!)
        vectorPersona2 = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion1.id!!)
        //animal
        vectorAnimal1 = vectorServiceImpl.crearVector(TipoDeVector.Animal,ubicacion1.id!!)
        vectorAnimal2 = vectorServiceImpl.crearVector(TipoDeVector.Animal,ubicacion1.id!!)
        //insecto
        vectorInsecto1 = vectorServiceImpl.crearVector(TipoDeVector.Insecto,ubicacion1.id!!)
        vectorInsecto2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto,ubicacion1.id!!)


        //dataServiceSpring.crearSetDeDatosIniciales()
    }

    @Test
    fun cuandoSeCreaUnVectorSeLeAsignaUnId(){
        assertNotNull(vectorPersona1.id)
    }
    //@Test
    fun siSeIntentanContagiarVectoresConUnVectorConIdInexistenteFalla() {
        val vectorSinID = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion1.id!!)

        //vectorServiceImpl.borrarVector(vectorSinID.id!!)

        dataServiceSpring.eliminarTodo()

        val vectores = listOf(vectorPersona1,vectorPersona2)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.contagiar(vectorSinID, vectores)
        }
    }

    //@Test
    fun siSeIntentanContagiarVectoresConUnVectorInvalidoEntoncesFalla() {
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest2")
        val vector2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vector1 = Vector(TipoDeVector.Persona, ubicacionCreada2)

        val vectores = listOf(vector2)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.contagiar(vector1, vectores)
        }
    }

    @Test
    fun siSeIntentaContagiarAUnaListaDeVectoresVaciaEntoncesNoSeHaceNada() {
        vectorServiceImpl.infectar(vectorPersona1,especie1!!)

        val vectores = emptyList<Vector>()

        assertDoesNotThrow{vectorServiceImpl.contagiar(vectorPersona1, vectores)}
    }

    @Test
    fun soloSePuedenContagiarVectoresInsectosDeVectoresPersonaOAnimalDeLoContrarioNoHaceNada() {

        //infecto un vector sano con una especie
        assertTrue(vectorInsecto2.estaSano())
        vectorServiceImpl.infectar(vectorInsecto2,especie1)
        assertFalse(vectorInsecto2.estaSano())

        //verifico que los vectores de los tres tipos estén sanos
        assertTrue(vectorPersona1.estaSano())
        assertTrue(vectorAnimal1.estaSano())
        assertTrue(vectorInsecto1.estaSano())
        val vectoresAContagiar = listOf(vectorPersona1,vectorAnimal1,vectorInsecto1)

        //contagio los vectores sanos con un vector insecto enfermo
        vectorServiceImpl.contagiar(vectorInsecto2, vectoresAContagiar)

        //verifico que los vectores persona y animal esten enfermos
        assertFalse(vectorPersona1.estaSano())
        assertFalse(vectorAnimal1.estaSano())

        //verifico que el vector insecto siga sano
        assertTrue(vectorInsecto1.estaSano())
    }

    @Test
    fun losVectoresPersonaSePuedenContagiarDeCualquierVector2() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorPersonaCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorPersonaCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAnimalCreado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val especie1 = especie1
        val especie2 = especie2
        val especie3 = especie3

        vectorServiceImpl.infectar(vectorPersonaCreado2, especie1)
        vectorServiceImpl.infectar(vectorAnimalCreado, especie2)
        vectorServiceImpl.infectar(vectorInsectoCreado, especie3)

        val vectores = listOf(vectorPersonaCreado1)

        Assertions.assertFalse(vectorPersonaCreado1.tieneEfermedad(especie1.id!!))
        Assertions.assertFalse(vectorPersonaCreado1.tieneEfermedad(especie2.id!!))
        Assertions.assertFalse(vectorPersonaCreado1.tieneEfermedad(especie3.id!!))
        Assertions.assertEquals(vectorPersonaCreado1.ubicacion.id!!, vectorPersonaCreado2.ubicacion.id!!)
        Assertions.assertEquals(vectorPersonaCreado1.ubicacion.id!!, vectorAnimalCreado.ubicacion.id!!)
        Assertions.assertEquals(vectorPersonaCreado1.ubicacion.id!!, vectorInsectoCreado.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorPersonaCreado2, vectores)
        vectorServiceImpl.contagiar(vectorAnimalCreado, vectores)
        vectorServiceImpl.contagiar(vectorInsectoCreado, vectores)

        Assertions.assertTrue(vectorPersonaCreado1.tieneEfermedad(especie1.id!!))
        Assertions.assertTrue(vectorPersonaCreado1.tieneEfermedad(especie2.id!!))
        Assertions.assertTrue(vectorPersonaCreado1.tieneEfermedad(especie3.id!!))
    }
    @Test
    fun losVectoresPersonaSePuedenContagiarDeCualquierVector() {

        //contagio tres vectores diferentes con tres especies diferentes
        //persona con especie1
        assertTrue(vectorPersona1.estaSano())
        vectorServiceImpl.infectar(vectorPersona1,especie1)
        assertFalse(vectorPersona1.estaSano())
        //animal con especie2
        assertTrue(vectorAnimal1.estaSano())
        vectorServiceImpl.infectar(vectorAnimal1,especie2)
        assertFalse(vectorAnimal1.estaSano())
        //insecto con especie3
        assertTrue(vectorInsecto1.estaSano())
        vectorServiceImpl.infectar(vectorInsecto1,especie3)
        assertFalse(vectorInsecto1.estaSano())

        //verifico que el vector persona a contagiar este sano
        assertTrue(vectorPersona2.estaSano())
        //val vectoresAContagiar = listOf(vectorPersona2)

        //contagio un vector persona sano con los tres vectores infectados
        vectorServiceImpl.contagiar(vectorPersona1, listOf(vectorPersona2))
        assertTrue(vectorPersona2.tieneEfermedad(especie1.id!!))
        assertFalse(vectorPersona2.tieneEfermedad(especie2.id!!))
        assertFalse(vectorPersona2.tieneEfermedad(especie3.id!!))

        vectorServiceImpl.contagiar(vectorAnimal1, listOf(vectorPersona2))
        vectorServiceImpl.contagiar(vectorInsecto1, listOf(vectorPersona2))

        //verifico que el vector persona se haya infectado con las tres especies
        assertTrue(vectorPersona2.tieneEfermedad(especie1.id!!))
        assertTrue(vectorPersona2.tieneEfermedad(especie2.id!!))
        assertTrue(vectorPersona2.tieneEfermedad(especie3.id!!))
    }

    @Test
    fun soloSePuedenContagiarAVectoresAnimalesConVectoresInsectoDeLoContrarioNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorAnimalCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorAnimalCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorPersonaCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorInsectoCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val unaEspecie = especie1

        vectorServiceImpl.infectar(vectorAnimalCreado2, unaEspecie)
        vectorServiceImpl.infectar(vectorPersonaCreado, unaEspecie)
        vectorServiceImpl.infectar(vectorInsectoCreado, unaEspecie)

        val vectores = listOf(vectorAnimalCreado1)

        assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorAnimalCreado2.ubicacion.id!!)
        Assertions.assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorPersonaCreado.ubicacion.id!!)
        Assertions.assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorInsectoCreado.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorAnimalCreado2, vectores)
        assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorPersonaCreado, vectores)
        assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorInsectoCreado, vectores)
        Assertions.assertTrue(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))
    }

    //@Test
    fun siSeIntentaContagiarAVectoresEnOtraUbicacionEntoncesNoSeHaceNada() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("enfermedadesTest2")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorCreado1, unaEspecie)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        assertFalse(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        assertFalse(vectorCreado3.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertNotEquals(vectorCreado1.ubicacion.id!!, vectorCreado2.ubicacion.id!!)
        Assertions.assertNotEquals(vectorCreado1.ubicacion.id!!, vectorCreado3.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        assertFalse(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        assertFalse(vectorCreado3.tieneEfermedad(unaEspecie.id!!))
    }

    //@Test
    fun siUnVectorNoTieneEnfermedadesYSeIntentaContagiarAOtrosVectoresEntoncesNoSeHaceNada() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertTrue(vectorCreado2.estaSano())
        Assertions.assertTrue(vectorCreado3.estaSano())

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertTrue(vectorCreado1.estaSano())
        Assertions.assertTrue(vectorCreado2.estaSano())
        Assertions.assertTrue(vectorCreado3.estaSano())
    }

    //@Test
    fun seContagianCorrectamenteALosVectoresConUnaEnfermedad() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val unaEspecie = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorCreado1, unaEspecie)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        assertFalse(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        assertFalse(vectorCreado3.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertTrue(vectorCreado2.tieneEfermedad(unaEspecie.id!!))
        Assertions.assertTrue(vectorCreado3.tieneEfermedad(unaEspecie.id!!))
    }

    //@Test
    fun seContagianCorrectamenteALosVectoresConMasDeUnaEnfermedad() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado2 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val vectorCreado3 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieRecuperada1 = especieServiceImpl.recuperarEspecie(1)
        val especieRecuperada2 = especieServiceImpl.recuperarEspecie(2)

        vectorServiceImpl.infectar(vectorCreado1, especieRecuperada1)
        vectorServiceImpl.infectar(vectorCreado1, especieRecuperada2)

        val vectores = listOf(vectorCreado2,vectorCreado3)

        assertFalse(vectorCreado2.tieneEfermedad(especieRecuperada1.id!!))
        assertFalse(vectorCreado3.tieneEfermedad(especieRecuperada1.id!!))
        assertFalse(vectorCreado2.tieneEfermedad(especieRecuperada2.id!!))
        assertFalse(vectorCreado3.tieneEfermedad(especieRecuperada2.id!!))

        vectorServiceImpl.contagiar(vectorCreado1, vectores)

        Assertions.assertTrue(vectorCreado2.tieneEfermedad(especieRecuperada1.id!!))
        Assertions.assertTrue(vectorCreado3.tieneEfermedad(especieRecuperada1.id!!))
        Assertions.assertTrue(vectorCreado2.tieneEfermedad(especieRecuperada2.id!!))
        Assertions.assertTrue(vectorCreado3.tieneEfermedad(especieRecuperada2.id!!))
    }

    //@Test
    fun noSePuedeCrearUnaEspecieConNombreVacioOConCaracteresEspeciales() {
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie#1", ubicacionCreada1.id!!)
        }
        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "especie-1", ubicacionCreada1.id!!)
        }
    }

    //@Test
    fun noSePuedeCrearUnaEspecieConNombreDePaisDeOrigenVacioOConCaracteresEspeciales() {
        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionCreada1.nombre = ""
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionCreada1.nombre = "ubicacion#88"
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }

        Assertions.assertThrows(NoPuedeEstarVacioOContenerCaracteresEspeciales::class.java) {
            ubicacionCreada1.nombre = "@ubicacion-1"
            patogenoService.agregarEspecie(patogenoCreado1.id!!, "", ubicacionCreada1.id!!)
        }
    }

    //@Test
    fun seInfectaAUnVectorConUnaEspecieCorrectamente() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)
        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        Assertions.assertTrue(vectorServiceImpl.enfermedades(vectorCreado.id!!).isEmpty())

        vectorServiceImpl.infectar(vectorCreado, especieRecuperada)

        val enfermedades = vectorServiceImpl.enfermedades(vectorCreado.id!!)

        Assertions.assertNotNull(enfermedades.find {
            it.id == especieRecuperada.id!! &&
                    it.nombre == especieRecuperada.nombre &&
                    it.paisDeOrigen == especieRecuperada.paisDeOrigen &&
                    it.patogeno.id!! == especieRecuperada.patogeno.id!!
        })
        Assertions.assertEquals(1, enfermedades.size)
    }

    //@Test
    fun noSePuedeInfectarAUnVectorInexistenteEnLaBDDConUnaEspecie() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        dataServiceSpring.eliminarTodo()

        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado1, especieCreada1)
        }
    }

    //@Test
    fun noSePuedeInfectarAUnVectorConUnaEspecieInexistenteEnLaBDD() {
        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        dataServiceSpring.eliminarTodo()

        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado, especieRecuperada)
        }
    }

    //@Test
    fun noSeLePuedePasarAInfectarUnObjetoVectorSinID() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        val vector = Vector(TipoDeVector.Persona, ubicacionCreada1)
        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vector, especieRecuperada)
        }
    }

    //@Test
    fun noSeLePuedePasarAInfectarUnObjetoEspecieSinID() {
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val patogeno = Patogeno("testEspecie")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val especie = Especie(patogenoCreado, "testEspecie", "unPaisCualquiera")

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado, especie)
        }
    }

    //@Test
    fun seRecuperanLasEnfermedadesDeUnVectorCorrectamenteYAlEstarSanoRetornaUnaListaVacia() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        Assertions.assertTrue(vectorServiceImpl.enfermedades(vectorCreado.id!!).isEmpty())

        val especieRecuperada = especieServiceImpl.recuperarEspecie(1)

        vectorServiceImpl.infectar(vectorCreado, especieRecuperada)

        val enfermedades = vectorServiceImpl.enfermedades(vectorCreado.id!!)

        Assertions.assertNotNull(enfermedades.find {
            it.id == especieRecuperada.id!! &&
            it.nombre == especieRecuperada.nombre &&
            it.paisDeOrigen == especieRecuperada.paisDeOrigen &&
            it.patogeno.id!! == especieRecuperada.patogeno.id!!
        })
        Assertions.assertEquals(1, enfermedades.size)
    }

    //@Test
    fun noSePuedenRecuperarLasEnfermedadesDeUnVectorInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.enfermedades(12312313)
        }
    }

    //@Test
    fun noSePuedeCrearUnVectorConUnaUbicacionInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.crearVector(TipoDeVector.Persona, 12312313)
        }
    }

    //@Test
    fun seRecuperaUnVectorConTodosSusDatosCorrectamente() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("testRecuperarVector")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada.id!!)
        val vectorRecuperado = vectorServiceImpl.recuperarVector(vectorCreado.id!!)

        Assertions.assertEquals(vectorRecuperado.ubicacion.id!!, vectorCreado.ubicacion.id!!)
        Assertions.assertEquals(vectorRecuperado.id!!, vectorCreado.id!!)
        Assertions.assertEquals(vectorRecuperado.tipo, vectorCreado.tipo)
        Assertions.assertEquals(vectorRecuperado.especies.size, vectorCreado.especies.size)
    }

    //@Test
    fun noSePuedeRecuperarUnVectorConUnIdInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.recuperarVector(29129229)
        }
    }

    //@Test
    fun seBorraUnVectorCorrectamenteDeLaBDD() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("testBorrarVector")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada.id!!)

        vectorServiceImpl.borrarVector(vectorCreado.id!!)

        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.recuperarVector(vectorCreado.id!!)
        }
    }

    //@Test
    fun noSePuedeBorrarUnVectorDeLaBDDConUnIdInexistente() {
        Assertions.assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.borrarVector(29129229)
        }
    }

    //@Test
    fun seRecuperanTodosLosVectoresCorrectamente() {
        dataServiceSpring.eliminarTodo()

        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("nombreCualquiera1")
        val vector1Creado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vector2Creado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        val vectoresRecuperados = vectorServiceImpl.recuperarTodos()

        Assertions.assertNotNull(vectoresRecuperados.find { it.id == vector1Creado.id!! })
        Assertions.assertNotNull(vectoresRecuperados.find { it.id == vector2Creado.id!! })

        Assertions.assertTrue(vectoresRecuperados.size == 2)
    }

    //@Test
    fun alRecuperarTodosLosVectoresDeUnaBDDVaciaEntoncesSeRetornaUnaListaVacia() {
        dataServiceSpring.eliminarTodo()

        val vectoresRecuperados = vectorServiceImpl.recuperarTodos()

        Assertions.assertTrue(vectoresRecuperados.isEmpty())
    }

    //@AfterEach
    fun eliminarModelo() {
        dataServiceSpring.eliminarTodo()
    }
}