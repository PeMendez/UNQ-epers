package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test



class VectorTest {

    private val ub1 = Ubicacion("Noruega")
    private val ub2 = Ubicacion("Suiza")
    private val ub3 = Ubicacion("Belgica")
    private val vectorPersonaSano = Vector(TipoDeVector.Persona, ub1)
    private val vectorPersonaEnfermo = Vector(TipoDeVector.Persona, ub1)
    private val vectorInsecto = Vector(TipoDeVector.Insecto, ub3)
    private val vectorAnimal = Vector(TipoDeVector.Animal, ub3)
    private val patogeno1 = Patogeno("Virus")
    private val especie1 = Especie(patogeno1, "Mercho", "Noruega")


    @BeforeEach
    fun setUp() {
        Random.switchModo(false)
    }

    @Test
    fun alInfectarUnVectorConUnaEspecieEstaSeGuarda() {

        vectorPersonaEnfermo.infectarCon(especie1)

        assertTrue(vectorPersonaEnfermo.especies.contains(especie1))
        assertEquals(1, vectorPersonaEnfermo.especies.size)

    }

    @Test
    fun alIntentarInfectarUnVectorConUnaEspecieQueYaEstaInfectadoNoSeDuplica() {

        vectorPersonaEnfermo.infectarCon(especie1)

        assertEquals(1, vectorPersonaEnfermo.especies.size)

        vectorPersonaEnfermo.infectarCon(especie1)

        assertEquals(1, vectorPersonaEnfermo.especies.size)


    }

    @Test
    fun esContagioExitosoTrue() {

        assertEquals(vectorPersonaSano.ubicacion.nombre, vectorPersonaEnfermo.ubicacion.nombre)
        assertEquals(vectorPersonaSano.tipo, vectorPersonaEnfermo.tipo)

        assertTrue(vectorPersonaSano.esContagioExitoso(vectorPersonaEnfermo, especie1))

    }

    @Test
    fun esContagioExitosoFalsePorNoEstarEnLaMismaUbicacion() {

        assertNotEquals(vectorInsecto.ubicacion.nombre, vectorPersonaEnfermo.ubicacion.nombre)

        assertFalse(vectorInsecto.esContagioExitoso(vectorPersonaEnfermo,especie1))
    }

    @Test
    fun esContagioExitosoFalsePorNoSerCompatibleDeTipo() {


        assertFalse(vectorAnimal.esContagioExitoso(vectorPersonaEnfermo,especie1))

    }

    @Test
    fun porcentajeDeContagioExitoso() {

        assertEquals(110, vectorPersonaSano.porcentajeDeContagioExitoso(especie1))
    }

    @Test
    fun estaSanoTrue() {

        assertTrue(vectorPersonaSano.estaSano())
    }

    @Test
    fun estaSanoFalse() {
        assertTrue(vectorPersonaSano.estaSano())
        vectorPersonaSano.infectarCon(especie1)
        assertFalse(vectorPersonaSano.estaSano())
    }

    @Test
    fun cuandoSeMueveUnVectorSeLeModificaSuUbicacion() {

        assertEquals("Noruega", vectorPersonaSano.ubicacion.nombre)

        vectorPersonaSano.mover(ub2)

        assertEquals("Suiza", vectorPersonaSano.ubicacion.nombre)

    }

}