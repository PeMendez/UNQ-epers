package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.NoPuedeEstarVacioOContenerCaracteresEspeciales
import org.junit.jupiter.api.*

class EspecieTest{

    @Test
    fun alQuererCrearUnaUbicacionConUnNombreVacioLanzaUnError(){
        val patogeno = Patogeno("Virus")
        val ex = assertThrows<NoPuedeEstarVacioOContenerCaracteresEspeciales> { Especie(patogeno,"","") }

        Assertions.assertEquals("El nombre o el pais no puede ser vacíos o contener caracteres especiales", ex.message)

    }

    @Test
    fun alQuererCrearUnaUbicacionConUnNombreConCaracteresEspecialesLanzaUnError(){
        val patogeno = Patogeno("Virus")
        val ex = assertThrows<NoPuedeEstarVacioOContenerCaracteresEspeciales> { Especie(patogeno,"QQ##","##") }

        Assertions.assertEquals("El nombre o el pais no puede ser vacíos o contener caracteres especiales", ex.message)

    }
}