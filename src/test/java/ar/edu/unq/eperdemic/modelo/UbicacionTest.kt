package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import org.junit.jupiter.api.*

class UbicacionTest{

    @Test
    fun alQuererCrearUnaUbicacionConUnNombreVacioLanzaUnError(){

        val ex = assertThrows<NoPuedeEstarVacioOContenerCaracteresEspeciales> { Ubicacion("") }

        Assertions.assertEquals("El nombre no puede ser vacío o contener caracteres especiales", ex.message)

    }

    @Test
    fun alQuererCrearUnaUbicacionConUnNombreConCaracteresEspecialesLanzaUnError(){

        val ex = assertThrows<NoPuedeEstarVacioOContenerCaracteresEspeciales> { Ubicacion("@@##") }

        Assertions.assertEquals("El nombre no puede ser vacío o contener caracteres especiales", ex.message)

    }
}