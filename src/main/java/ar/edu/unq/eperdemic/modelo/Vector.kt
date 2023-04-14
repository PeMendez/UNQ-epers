package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Vector(@Id
             @GeneratedValue(strategy = GenerationType.IDENTITY)
             var id: Long?,
             var tipo: TipoDeVector,
             @OneToOne
             var ubicacion: Ubicacion,
             @OneToMany
             var especies : MutableList<Especie> = mutableListOf<Especie>()) {


    fun intentarInfectar(vectorInfectado: Vector, especie: Especie) {
        if (esContagioExitoso(vectorInfectado,especie)) {
            especies.add(especie)
        }
    }

    fun esContagioExitoso(vectorInfectado: Vector, especie: Especie): Boolean {
        return ubicacion.nombre == vectorInfectado.ubicacion.nombre
                && tipo.puedeSerInfectado(vectorInfectado.tipo)
                && capacidadDeDefensa() < porcentajeDeContagioExitoso(especie)
    }

    fun porcentajeDeContagioExitoso(especie:Especie): Int{
        return (1..10).random() + especie.patogeno.capacidadDeContagio
    }

    fun capacidadDeDefensa():Int{
        return if (estaSano()) 0 else especies.map { e -> e.patogeno.capacidadDeDefensa }.average().toInt()
    }

    private fun estaSano(): Boolean {
        return especies.isEmpty()
    }

}

enum class TipoDeVector {
    Persona {
        override fun puedeSerInfectado(vector: TipoDeVector): Boolean {
            return true
        }
    },
    Insecto{
        override fun puedeSerInfectado(vector: TipoDeVector): Boolean {
            return this != vector
        }
    },
    Animal{
        override fun puedeSerInfectado(vector: TipoDeVector): Boolean {
            return this.ordinal == vector.ordinal +1
        }
    };
    abstract fun puedeSerInfectado(vector: TipoDeVector): Boolean
}