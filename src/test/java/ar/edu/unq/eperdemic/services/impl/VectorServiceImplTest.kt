package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.TipoDeVector
import org.junit.jupiter.api.Test
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.NoExisteElid
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
    @Autowired
    private lateinit var mutacionService: MutacionServiceImpl

    lateinit var ubicacion1: Ubicacion
    lateinit var ubicacion2: Ubicacion
    lateinit var vectorCarnada: Vector
    lateinit var vectorPersona1: Vector
    lateinit var vectorPersona2: Vector
    lateinit var vectorPersona3: Vector
    lateinit var vectorPersona4: Vector
    lateinit var vectorAnimal1: Vector
    lateinit var vectorAnimal2: Vector
    lateinit var vectorInsecto1: Vector
    lateinit var vectorInsecto2: Vector
    lateinit var patogeno1: Patogeno
    lateinit var especie1: Especie
    lateinit var especie2: Especie
    lateinit var especie3: Especie

    @BeforeEach
    fun setUp() {
        Random.switchModo(false)

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
        vectorPersona1 = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        vectorPersona2 = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        vectorPersona3 = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        vectorPersona4 = vectorServiceImpl.crearVector(TipoDeVector.Persona,ubicacion2.id!!)
        //animal
        vectorAnimal1 = vectorServiceImpl.crearVector(TipoDeVector.Animal,ubicacion2.id!!)
        vectorAnimal2 = vectorServiceImpl.crearVector(TipoDeVector.Animal,ubicacion2.id!!)
        //insecto
        vectorInsecto1 = vectorServiceImpl.crearVector(TipoDeVector.Insecto,ubicacion2.id!!)
        vectorInsecto2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto,ubicacion2.id!!)
        dataServiceSpring.crearSetDeDatosIniciales()
    }

    @Test
    fun cuandoSeCreaUnVectorSeLeAsignaUnId(){
        assertNotNull(vectorPersona1.id)
    }

    @Test
    fun siSeIntentanContagiarVectoresConUnVectorConIdInexistenteFalla() {
        val vectorSinID = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacion1.id!!)

        vectorServiceImpl.borrarVector(vectorSinID.id!!)

        val vectores = listOf(vectorPersona1,vectorPersona2)

        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.contagiar(vectorSinID, vectores)
        }
    }

    @Test
    fun siSeIntentaContagiarAUnaListaDeVectoresVaciaEntoncesNoSeHaceNada() {
        vectorServiceImpl.infectar(vectorPersona1,especie1)

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

        val vectorCon1Especie = vectorServiceImpl.recuperarVector(vectorPersona2.id!!)
        vectorServiceImpl.contagiar(vectorAnimal1, listOf(vectorCon1Especie))

        val vectorCon2Especies = vectorServiceImpl.recuperarVector(vectorPersona2.id!!)
        vectorServiceImpl.contagiar(vectorInsecto1, listOf(vectorCon2Especies))

        //verifico que el vector persona se haya infectado con las tres especies
        assertTrue(vectorCon2Especies.tieneEfermedad(especie1.id!!))
        assertTrue(vectorCon2Especies.tieneEfermedad(especie2.id!!))
        assertTrue(vectorCon2Especies.tieneEfermedad(especie3.id!!))
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
        assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorAnimalCreado2.ubicacion.id!!)
        assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorPersonaCreado.ubicacion.id!!)
        assertEquals(vectorAnimalCreado1.ubicacion.id!!, vectorInsectoCreado.ubicacion.id!!)

        vectorServiceImpl.contagiar(vectorAnimalCreado2, vectores)
        assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorPersonaCreado, vectores)
        assertFalse(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))

        vectorServiceImpl.contagiar(vectorInsectoCreado, vectores)
        assertTrue(vectorAnimalCreado1.tieneEfermedad(unaEspecie.id!!))
    }

    @Test
    fun siSeIntentaContagiarAVectoresEnOtraUbicacionEntoncesNoSeHaceNada() {
        val vectorInfectado = vectorServiceImpl.recuperarVector(vectorCarnada.id!!)
        assertFalse(vectorInfectado.estaSano())
        assertTrue(vectorPersona1.estaSano())

        assertNotEquals(vectorPersona1.ubicacion.nombre,vectorInfectado.ubicacion.nombre)

        vectorServiceImpl.contagiar(vectorInfectado, listOf(vectorPersona1))

        assertTrue(vectorPersona1.estaSano())
    }

    @Test
    fun siUnVectorNoTieneEnfermedadesYSeIntentaContagiarAOtrosVectoresEntoncesNoSeHaceNada() {
        assertTrue(vectorAnimal1.estaSano())
        assertTrue(vectorPersona1.estaSano())
        assertTrue(vectorPersona2.estaSano())

        vectorServiceImpl.contagiar(vectorAnimal1, listOf(vectorPersona1,vectorPersona2))

        assertTrue(vectorPersona1.estaSano())
        assertTrue(vectorPersona2.estaSano())
    }

    @Test
    fun seContagianCorrectamenteALosVectoresConUnaEnfermedad() {
        vectorServiceImpl.infectar(vectorAnimal1, especie1)

        assertFalse(vectorAnimal1.estaSano())
        assertTrue(vectorPersona1.estaSano())
        assertTrue(vectorPersona2.estaSano())

        vectorServiceImpl.contagiar(vectorAnimal1, listOf(vectorPersona1,vectorPersona2))

        assertFalse(vectorPersona1.estaSano())
        assertFalse(vectorPersona2.estaSano())
    }

    @Test
    fun seContagianCorrectamenteALosVectoresConMasDeUnaEnfermedad() {
        assertTrue(vectorAnimal1.estaSano())

        vectorServiceImpl.infectar(vectorAnimal1,especie1)
        val vectorAniminalInfectado = vectorServiceImpl.recuperarVector(vectorAnimal1.id!!)
        vectorServiceImpl.infectar(vectorAniminalInfectado,especie2)
        val vectorAnimalInfectadoConDos = vectorServiceImpl.recuperarVector(vectorAniminalInfectado.id!!)

        assertTrue(vectorAnimalInfectadoConDos.tieneEfermedad(especie1.id!!))
        assertTrue(vectorAnimalInfectadoConDos.tieneEfermedad(especie2.id!!))

        assertTrue(vectorPersona1.estaSano())
        assertTrue(vectorPersona2.estaSano())

        vectorServiceImpl.contagiar(vectorAnimalInfectadoConDos, listOf(vectorPersona1,vectorPersona2))

        assertTrue(vectorPersona1.tieneEfermedad(especie1.id!!))
        assertTrue(vectorPersona1.tieneEfermedad(especie2.id!!))

        assertTrue(vectorPersona2.tieneEfermedad(especie1.id!!))
        assertTrue(vectorPersona2.tieneEfermedad(especie2.id!!))
    }

    @Test
    fun seInfectaAUnVectorConUnaEspecieCorrectamente() {

        assertTrue(vectorPersona1.estaSano())
        vectorServiceImpl.infectar(vectorPersona1,especie1)
        assertTrue(vectorPersona1.tieneEfermedad(especie1.id!!))

    }

    @Test
    fun noSePuedeInfectarAUnVectorInexistenteEnLaBDDConUnaEspecie() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado1 = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        dataServiceSpring.eliminarTodo()

        val patogeno1 = Patogeno("testEspecie")
        val patogenoCreado1 = patogenoService.crearPatogeno(patogeno1)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie")
        vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val especieCreada1 = patogenoService.agregarEspecie(patogenoCreado1.id!!, "cualquierNombre", ubicacionCreada1.id!!)

        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado1, especieCreada1)
        }
    }

    @Test
    fun noSePuedeInfectarAUnVectorConUnaEspecieInexistenteEnLaBDD() {
        val especieRecuperada = especieServiceImpl.recuperarEspecie(especie1.id!!)

        dataServiceSpring.eliminarTodo()

        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("enfermedadesTest")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada.id!!)

        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.infectar(vectorCreado, especieRecuperada)
        }
    }

    @Test
    fun seRecuperanLasEnfermedadesDeUnVectorInfectadoCorrectamente() {
        val vectorInfectado = vectorServiceImpl.recuperarVector(vectorCarnada.id!!)

        assertTrue(vectorInfectado.tieneEfermedad(especie1.id!!))
        assertTrue(vectorInfectado.tieneEfermedad(especie2.id!!))
        assertTrue(vectorInfectado.tieneEfermedad(especie3.id!!))

        val enfermedadesDeVector= vectorServiceImpl.enfermedades(vectorInfectado.id!!).map { it.id }

        assertTrue(enfermedadesDeVector.containsAll(listOf(especie1.id,especie2.id,especie3.id)))

    }

    @Test
    fun alQuererRecuperarLasEnfermedadesDeUnVectorSanoRetornaUnaListaVacia(){
        assertTrue(vectorPersona1.estaSano())
        assertTrue(vectorServiceImpl.enfermedades(vectorPersona1.id!!).isEmpty())
    }

    @Test
    fun noSePuedenRecuperarLasEnfermedadesDeUnVectorInexistente() {
        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.enfermedades(-12312313)
        }
    }

    @Test
    fun noSePuedeCrearUnVectorConUnaUbicacionInexistente() {
        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.crearVector(TipoDeVector.Persona, -123)
        }
    }

    @Test
    fun seRecuperaUnVectorConTodosSusDatosCorrectamente() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("testRecuperarVector")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada.id!!)
        val vectorRecuperado = vectorServiceImpl.recuperarVector(vectorCreado.id!!)

        assertEquals(vectorRecuperado.ubicacion.id!!, vectorCreado.ubicacion.id!!)
        assertEquals(vectorRecuperado.id!!, vectorCreado.id!!)
        assertEquals(vectorRecuperado.tipo, vectorCreado.tipo)
        assertEquals(vectorRecuperado.especies.size, vectorCreado.especies.size)
    }

    @Test
    fun noSePuedeRecuperarUnVectorConUnIdInexistente() {
        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.recuperarVector(-123)
        }
    }

    @Test
    fun seBorraUnVectorCorrectamenteDeLaBDD() {
        val ubicacionCreada = ubicacionServiceImpl.crearUbicacion("testBorrarVector")
        val vectorCreado = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada.id!!)

        vectorServiceImpl.borrarVector(vectorCreado.id!!)

        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.recuperarVector(vectorCreado.id!!)
        }
    }

    @Test
    fun noSePuedeBorrarUnVectorDeLaBDDConUnIdInexistente() {
        assertThrows(NoExisteElid::class.java) {
            vectorServiceImpl.borrarVector(-123)
        }
    }

    @Test
    fun seRecuperanTodosLosVectoresCorrectamente() {
        dataServiceSpring.eliminarTodo()

        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("nombreCualquiera1")
        val vector1Creado = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vector2Creado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        val vectoresRecuperados = vectorServiceImpl.recuperarTodos().map { it.id }

        assertTrue(vectoresRecuperados.containsAll(listOf(vector1Creado.id,vector2Creado.id)))
    }

    @Test
    fun alRecuperarTodosLosVectoresDeUnaBDDVaciaEntoncesSeRetornaUnaListaVacia() {
        dataServiceSpring.eliminarTodo()

        val vectoresRecuperados = vectorServiceImpl.recuperarTodos()

        assertTrue(vectoresRecuperados.isEmpty())
    }

    // --------------------- TESTS DE MUTACIONES ---------------------

    @Test
    fun cuandoSeContagiaUnVectorConUnaEspecieQueContieneUnaMutacionEntoncesElVectorPuedeContraerla() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)
        val vectorInfectado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        val mutacionAAgregar = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar)

        vectorServiceImpl.infectar(vectorInfectado, especieCreada)

        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorInfectadoRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)

        val vectorAInfectarLista = listOf(vectorAInfectar)

        assertNull(vectorInfectadoRecuperado.mutaciones.find { it.id == mutacion.id })

        vectorServiceImpl.contagiar(vectorInfectadoRecuperado, vectorAInfectarLista)

        assertNotNull(vectorInfectadoRecuperado.mutaciones.find { it.id == mutacion.id })
        assertNotNull(vectorInfectadoRecuperado.especies.find { it.id == especieCreada.id!! })
    }

    @Test
    fun cuandoSeContagiaUnVectorConUnaEspecieSinMutacionesEntoncesElVectorNoContraeMutaciones() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)
        val vectorInfectado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        vectorServiceImpl.infectar(vectorInfectado, especieCreada)

        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorInfectadoRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)

        val vectorAInfectarLista = listOf(vectorAInfectar)

        assertTrue(vectorInfectadoRecuperado.mutaciones.isEmpty())

        vectorServiceImpl.contagiar(vectorInfectadoRecuperado, vectorAInfectarLista)

        assertTrue(vectorInfectadoRecuperado.mutaciones.isEmpty())
        assertNotNull(vectorInfectadoRecuperado.especies.find { it.id == especieCreada.id!! })
    }

    @Test
    fun cuandoSeContagiaUnVectorConUnaEspecieQueContieneMasDeUnaMutacionEntoncesElVectorContraeSoloUna() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreada = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)
        val vectorInfectado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacionAAgregar2 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion1 = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar1)
        val mutacion2 = mutacionService.agregarMutacion(especieCreada.id!!, mutacionAAgregar2)
        val especieRecuperada = especieServiceImpl.recuperarEspecie(especieCreada.id!!)

        vectorServiceImpl.infectar(vectorInfectado, especieRecuperada)

        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorInfectadoRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)

        val vectorAInfectarLista = listOf(vectorAInfectar)

        assertNull(vectorInfectadoRecuperado.mutaciones.find { it.id == mutacion1.id || it.id == mutacion2.id!!})
        assertTrue(especieRecuperada.mutaciones.size == 2)

        vectorServiceImpl.contagiar(vectorInfectadoRecuperado, vectorAInfectarLista)

        assertTrue(vectorInfectadoRecuperado.mutaciones.size == 1)
        assertNotNull(vectorInfectadoRecuperado.mutaciones.find { it.id == mutacion1.id || it.id == mutacion2.id!!})
        assertNotNull(vectorInfectadoRecuperado.especies.find { it.id == especieCreada.id!! })
    }

    @Test
    fun cuandoUnVectorContraeSupresionBiomecanicaSePuedenEliminarLasEspeciesQueTenganMenorNivelDeDefensa() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val patogeno2 = Patogeno("testEspecie2")
        patogeno2.capacidadDeDefensa = 0
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie2")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreadaSinMutacion = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre2", ubicacionCreada2.id!!)

        val vectorInfectado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        vectorServiceImpl.infectar(vectorInfectado, especieCreadaConMutacion)
        val vectorRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)
        vectorServiceImpl.infectar(vectorRecuperado, especieCreadaSinMutacion)

        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        val vectorInfectadoRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)

        assertTrue(vectorInfectadoRecuperado.especies.size == 2)
        assertNotNull(vectorInfectadoRecuperado.especies.find { e -> e.mutaciones.any { m -> m.id == mutacion1.id!! } })

        vectorServiceImpl.contagiar(vectorInfectadoRecuperado, vectorAInfectarLista)

        assertTrue(vectorInfectadoRecuperado.especies.size == 1)
    }

    @Test
    fun cuandoUnVectorContraeSupresionBiomecanicaLasEspeciesConAltoNivelDeDefensaPuedenNoSerEliminadas() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val patogeno2 = Patogeno("testEspecie2")
        patogeno2.capacidadDeDefensa = 200
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie2")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreadaSinMutacion = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre2", ubicacionCreada2.id!!)

        val vectorInfectado = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        vectorServiceImpl.infectar(vectorInfectado, especieCreadaConMutacion)
        val vectorRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)
        vectorServiceImpl.infectar(vectorRecuperado, especieCreadaSinMutacion)

        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        val vectorInfectadoRecuperado = vectorServiceImpl.recuperarVector(vectorInfectado.id!!)

        assertTrue(vectorInfectadoRecuperado.especies.size == 2)
        assertNotNull(vectorInfectadoRecuperado.especies.find { e -> e.mutaciones.any { m -> m.id == mutacion1.id!! } })

        vectorServiceImpl.contagiar(vectorInfectadoRecuperado, vectorAInfectarLista)

        assertTrue(vectorInfectadoRecuperado.especies.size == 2)
    }

    @Test
    fun cuandoUnVectorContrajoSupresionBiomecanicaYSeLoIntentaContagierConUnaEspecieConBajoNivelDeDefensaEntoncesPuedeNoSerContagiado() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val patogeno2 = Patogeno("testEspecie2")
        patogeno2.capacidadDeDefensa = 0
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie2")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreadaDebil = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre2", ubicacionCreada2.id!!)


        val vectorConSupresion = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        vectorServiceImpl.infectar(vectorConSupresion, especieCreadaConMutacion)

        val vectorConSupresionRecuperado = vectorServiceImpl.recuperarVector(vectorConSupresion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConSupresionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConSupresionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorConSupresionALista = listOf(vectorConSupresionRecuperado)
        val vectorInfectadoConEspecieDebil = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        vectorServiceImpl.infectar(vectorInfectadoConEspecieDebil, especieCreadaDebil)
        val vectorInfectadoConEspecieDebilRec = vectorServiceImpl.recuperarVector(vectorInfectadoConEspecieDebil.id!!)

        assertTrue(vectorConSupresion.especies.size == 1)

        vectorServiceImpl.contagiar(vectorInfectadoConEspecieDebilRec, vectorConSupresionALista)

        assertTrue(vectorConSupresion.especies.size == 1)
    }

    @Test
    fun cuandoUnVectorContrajoSupresionBiomecanicaYSeLoIntentaContagierConUnaEspecieConAltoNivelDeDefensaEntoncesPuedeSerContagiado() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val patogeno2 = Patogeno("testEspecie2")
        patogeno2.capacidadDeDefensa = 200
        val patogenoCreado2 = patogenoService.crearPatogeno(patogeno2)
        val ubicacionCreada2 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie2")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val especieCreadaFuerte = patogenoService.agregarEspecie(patogenoCreado2.id!!, "cualquierNombre2", ubicacionCreada2.id!!)


        val vectorConSupresion = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        vectorServiceImpl.infectar(vectorConSupresion, especieCreadaConMutacion)

        val vectorConSupresionRecuperado = vectorServiceImpl.recuperarVector(vectorConSupresion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConSupresionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConSupresionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorConSupresionALista = listOf(vectorConSupresionRecuperado)
        val vectorInfectadoConEspecieFuerte = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada2.id!!)
        vectorServiceImpl.infectar(vectorInfectadoConEspecieFuerte, especieCreadaFuerte)
        val vectorInfectadoConEspecieFuerteRec = vectorServiceImpl.recuperarVector(vectorInfectadoConEspecieFuerte.id!!)

        assertTrue(vectorConSupresion.especies.size == 1)

        vectorServiceImpl.contagiar(vectorInfectadoConEspecieFuerteRec, vectorConSupresionALista)

        val vectorConSupresionContagiado = vectorServiceImpl.recuperarVector(vectorConSupresion.id!!)

        assertTrue(vectorConSupresionContagiado.especies.size == 2)
    }

    @Test
    fun cuandoUnVectorInsectoContrajoBioalteracionGeneticaInsectoEntoncesPuedeContagiarAVectoresInsectoConLaEnfermedadDeLaMutacion() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.BioalteracionGenetica)
        mutacionAAgregar1.tipoDeMutacion = TipoDeMutacion.BioalteracionGenetica
        mutacionAAgregar1.tipoDeVector = TipoDeVector.Insecto
        mutacionAAgregar1.potenciaDeMutacion = null
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val vectorConBioalteracion = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracion, especieCreadaConMutacion)

        val vectorConBioalteracionRecuperado = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConBioalteracionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorAInfectar2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val vectorAInfectar2Lista = listOf(vectorAInfectar2)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectar2Lista)

        val vectorAInfectarRec = vectorServiceImpl.recuperarVector(vectorAInfectar2.id!!)

       assertNotNull(vectorAInfectarRec.especies.find { e -> e.id!! == especieCreadaConMutacion.id!! })
    }

    @Test
    fun cuandoUnVectorAnimalContrajoBioalteracionGeneticaAnimalEntoncesPuedeContagiarAVectoresAnimalConLaEnfermedadDeLaMutacion() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion()
        mutacionAAgregar1.tipoDeMutacion = TipoDeMutacion.BioalteracionGenetica
        mutacionAAgregar1.tipoDeVector = TipoDeVector.Animal
        mutacionAAgregar1.potenciaDeMutacion = null
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val vectorConBioalteracion = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracion, especieCreadaConMutacion)

        val vectorConBioalteracionRecuperado = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConBioalteracionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorAInfectar2 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorAInfectar2Lista = listOf(vectorAInfectar2)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectar2Lista)

        val vectorAInfectarRec = vectorServiceImpl.recuperarVector(vectorAInfectar2.id!!)

        assertNotNull(vectorAInfectarRec.especies.find { e -> e.id!! == especieCreadaConMutacion.id!! })
    }

    @Test
    fun cuandoUnVectorPersonaContrajoBioalteracionGeneticaAnimalEntoncesPuedeContagiarAVectoresAnimalConLaEnfermedadDeLaMutacion() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion()
        mutacionAAgregar1.tipoDeMutacion = TipoDeMutacion.BioalteracionGenetica
        mutacionAAgregar1.tipoDeVector = TipoDeVector.Animal
        mutacionAAgregar1.potenciaDeMutacion = null
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val vectorConBioalteracion = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracion, especieCreadaConMutacion)

        val vectorConBioalteracionRecuperado = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConBioalteracionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorAInfectar2 = vectorServiceImpl.crearVector(TipoDeVector.Animal, ubicacionCreada1.id!!)
        val vectorAInfectar2Lista = listOf(vectorAInfectar2)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectar2Lista)

        val vectorAInfectarRec = vectorServiceImpl.recuperarVector(vectorAInfectar2.id!!)

        assertNotNull(vectorAInfectarRec.especies.find { e -> e.id!! == especieCreadaConMutacion.id!! })
    }

    @Test
    fun cuandoUnTipoDeVector1NoPuedeContagiarAOtroTipoDeVector2YMutaConBioalteracionGeneticaDistintaATipoDeVector2EntoncesSigueSinPoderContagiarlo() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion()
        mutacionAAgregar1.tipoDeMutacion = TipoDeMutacion.BioalteracionGenetica
        mutacionAAgregar1.tipoDeVector = TipoDeVector.Persona
        mutacionAAgregar1.potenciaDeMutacion = null
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val vectorConBioalteracion = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracion, especieCreadaConMutacion)

        val vectorConBioalteracionRecuperado = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConBioalteracionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorAInfectar2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val vectorAInfectar2Lista = listOf(vectorAInfectar2)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectar2Lista)

        val vectorAInfectarRec = vectorServiceImpl.recuperarVector(vectorAInfectar2.id!!)

        assertNull(vectorAInfectarRec.especies.find { e -> e.id!! == especieCreadaConMutacion.id!! })
    }

    @Test
    fun cuandoUnTipoDeVector1NoPuedeContagiarAOtroTipoDeVector2YMutaConBioalteracionGeneticaATipoDeVector2EntoncesSoloPuedeContagiarloConLaEnfermedadDeLaMutacion() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)
        val especieCreada2 = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre2", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion()
        mutacionAAgregar1.tipoDeMutacion = TipoDeMutacion.BioalteracionGenetica
        mutacionAAgregar1.tipoDeVector = TipoDeVector.Insecto
        mutacionAAgregar1.potenciaDeMutacion = null
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val vectorConBioalteracion = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracion, especieCreadaConMutacion)
        val vectorConBioalteracionRecuperado1 = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracionRecuperado1, especieCreada2)

        val vectorConBioalteracionRecuperado2 = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado2, vectorAInfectarLista)

        assertNotNull(vectorConBioalteracionRecuperado2.mutaciones.find { m -> m.id!! == mutacion1.id!! })

        val vectorAInfectar2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val vectorAInfectar2Lista = listOf(vectorAInfectar2)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado2, vectorAInfectar2Lista)

        assertTrue(vectorConBioalteracionRecuperado2.especies.size == 2)

        val vectorAInfectarRec = vectorServiceImpl.recuperarVector(vectorAInfectar2.id!!)

        assertNotNull(vectorAInfectarRec.especies.find { e -> e.id!! == especieCreadaConMutacion.id!! })
        assertNull(vectorAInfectarRec.especies.find { e -> e.id!! == especieCreada2.id!! })
    }

    @Test
    fun siSeIntentaContraerDosVecesUnaMismaMutacionDeUnaMismaEspecieAUnVectorEntoncesSeContraeSoloUnaVez() {
        val patogeno = Patogeno("testEspecie1")
        val patogenoCreado = patogenoService.crearPatogeno(patogeno)
        val ubicacionCreada1 = ubicacionServiceImpl.crearUbicacion("ubicacionTestEspecie1")
        vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val especieCreadaConMutacion = patogenoService.agregarEspecie(patogenoCreado.id!!, "cualquierNombre1", ubicacionCreada1.id!!)

        val mutacionAAgregar1 = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val mutacion1 = mutacionService.agregarMutacion(especieCreadaConMutacion.id!!, mutacionAAgregar1)

        val vectorConBioalteracion = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        vectorServiceImpl.infectar(vectorConBioalteracion, especieCreadaConMutacion)

        val vectorConBioalteracionRecuperado = vectorServiceImpl.recuperarVector(vectorConBioalteracion.id!!)
        val vectorAInfectar = vectorServiceImpl.crearVector(TipoDeVector.Persona, ubicacionCreada1.id!!)
        val vectorAInfectarLista = listOf(vectorAInfectar)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectarLista)

        assertNotNull(vectorConBioalteracionRecuperado.mutaciones.find { m -> m.id!! == mutacion1.id!! })
        assertTrue(vectorConBioalteracionRecuperado.mutaciones.size == 1)

        val vectorAInfectar2 = vectorServiceImpl.crearVector(TipoDeVector.Insecto, ubicacionCreada1.id!!)
        val vectorAInfectar2Lista = listOf(vectorAInfectar2)
        vectorServiceImpl.contagiar(vectorConBioalteracionRecuperado, vectorAInfectar2Lista)

        assertTrue(vectorConBioalteracionRecuperado.mutaciones.size == 1)
    }

    @AfterEach
    fun eliminarModelo() {
        dataServiceSpring.eliminarTodo()
    }
}