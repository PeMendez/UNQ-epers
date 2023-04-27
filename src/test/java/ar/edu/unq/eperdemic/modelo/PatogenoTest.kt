package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
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

    @Test
    fun alQuererCrearUnPatogenoConUnNombreVacioLanzaUnError(){

        val ex = assertThrows<NoPuedeEstarVacioOContenerCaracteresEspeciales> { Patogeno("")  }

        Assertions.assertEquals("El tipo no puede ser vacío o contener caracteres especiales", ex.message)

    }

    @Test
    fun alQuererCrearUnPatogenoConUnNombreConCaracteresEspecialesLanzaUnError(){

        val ex = assertThrows<NoPuedeEstarVacioOContenerCaracteresEspeciales> { Patogeno("@@##")  }

        Assertions.assertEquals("El tipo no puede ser vacío o contener caracteres especiales", ex.message)

    }
}