package ar.edu.unq.eperdemic.modelo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MutacionTest {

    @Test
    fun seLeAsignaUnaEspecieAUnaMutacionCorrectamente() {
        val mutacion = Mutacion(TipoDeMutacion.SupresionBiomecanica)
        val especie = Especie()
        mutacion.addEspecie(especie)

        Assertions.assertEquals(especie, mutacion.especie)
    }

    @Test
    fun unaMutacionConoceSuTipoCorrectamente() {
        val mutacion = Mutacion(TipoDeMutacion.BioalteracionGenetica)

        Assertions.assertEquals(mutacion.tipoDeMutacion, TipoDeMutacion.BioalteracionGenetica)
    }
}