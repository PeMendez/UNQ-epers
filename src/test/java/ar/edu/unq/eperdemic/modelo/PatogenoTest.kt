package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.*


class PatogenoTest {

    @Test
    fun alCrearleUnaEspecieAUnPatogenoSeIncrementaSuCantidadDeEspecies() {
        val patogeno = Patogeno("virus")
        patogeno.crearEspecie("especieViolenta", "Canada")

        Assertions.assertEquals(1, patogeno.cantidadDeEspecies)
    }

    @Test
    fun alCrearleUnaEspecieAUnPatogenoSeAgregaEnSuSet() {
        val patogeno = Patogeno("virus")
        val especie = patogeno.crearEspecie("especieViolenta", "Canada")

        Assertions.assertTrue(patogeno.especies.contains(especie))
    }
}