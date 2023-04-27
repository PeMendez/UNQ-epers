package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Vector(var tipo: TipoDeVector,
             @ManyToOne
             var ubicacion: Ubicacion,
             @ManyToMany(fetch = FetchType.EAGER)
             var especies : MutableSet<Especie> = mutableSetOf()) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


    fun intentarInfectar(vectorInfectado: Vector, especie: Especie) {
        if (esContagioExitoso(vectorInfectado,especie)) {
            this.infectarCon(especie)
        }
    }

    fun infectarCon(especie: Especie) {
        especies.add(especie)
    }

    fun esContagioExitoso(vectorInfectado: Vector, especie: Especie): Boolean {
        return ubicacion.nombre == vectorInfectado.ubicacion.nombre
                && tipo.puedeSerInfectado(vectorInfectado.tipo)
                && Random.decidir(100) < porcentajeDeContagioExitoso(especie)
    }

    fun porcentajeDeContagioExitoso(especie:Especie): Int{
        return Random.decidir(10) + especie.patogeno.capacidadDeContagio
    }

    fun estaSano() : Boolean {
        return especies.isEmpty()
    }

    fun tieneEfermedad(especieId : Long) : Boolean {

        return especies.filter { e -> e.id == especieId}.isNotEmpty()
    }

    fun mover(ubicacion: Ubicacion)  {
        this.ubicacion = ubicacion

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