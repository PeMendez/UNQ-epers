package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.*


class PatogenoTest {

    @Test
    fun alCrearleUnaEspecieAUnPatogenoSeIncrementaSuCantidadDeEspecies() {
        val patogeno = Patogeno("virus")
        val cantEspeciesDelPatogeno = patogeno.cantidadDeEspecies
        patogeno.crearEspecie("especieViolenta", "Canada")

        Assertions.assertEquals(cantEspeciesDelPatogeno, patogeno.cantidadDeEspecies - 1)
    }
}