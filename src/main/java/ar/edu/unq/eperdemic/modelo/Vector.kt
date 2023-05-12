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

    @ManyToMany(fetch = FetchType.EAGER)
    var mutaciones: MutableSet<Mutacion> = mutableSetOf()

    fun intentarInfectarConEspecies(especies: List<Especie>, vectorAInfectar: Vector) {
        especies.forEach { e ->
            this.intentarInfectar(vectorAInfectar, e)
        }
    }

    fun intentarInfectar(vectorAInfectar: Vector, especie: Especie) {
        if (esContagioExitoso(vectorAInfectar, especie)) {
            vectorAInfectar.serInfectadoCon(especie)
            this.intentarMutar(especie)
        }
    }


    fun intentarMutar(especie: Especie) {
        if (especie.mutacionExitosa()) {
            val mutacionContraida = especie.mutar()
            this.agregarMutacion(mutacionContraida)
        }
    }

    private fun agregarMutacion(mutacion: Mutacion) {
        if (mutacion.tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
            mutacion.haceMagia(this)
        }
        mutaciones.add(mutacion)
    }

    fun esContagioExitoso(vectorAInfectar: Vector, especie: Especie): Boolean {
        return  ubicacion.nombre == vectorAInfectar.ubicacion.nombre
                && Random.decidir(100) < porcentajeDeContagioExitoso(especie)
                && supresionNoExitosa(vectorAInfectar, especie)
                && hayContagioPorTipo(especie, vectorAInfectar)
    }

    fun hayContagioPorTipo(especie: Especie, vectorAInfectar: Vector): Boolean {
        var mutacion = this.mutaciones.find { m ->
            m.tipoDeMutacion == TipoDeMutacion.BioalteracionGenetica
            && m.especie.id == especie.id
        }
        if (mutacion != null) {
            return  mutacion.tipoDeVector == vectorAInfectar.tipo
                    || vectorAInfectar.tipo.puedeSerInfectado(this.tipo)
        } else {
            return vectorAInfectar.tipo.puedeSerInfectado(this.tipo)
        }
    }

    private fun supresionNoExitosa(vectorAInfectar: Vector, especie: Especie): Boolean {
        var supresionNoExitosa = true
        vectorAInfectar.mutaciones.forEach { m ->
            if (m.tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
                supresionNoExitosa = supresionNoExitosa && m.potenciaDeMutacion!! < especie.capacidadDeDefensa()
            }
        }
        return supresionNoExitosa
    }

    fun porcentajeDeContagioExitoso(especie: Especie): Int {
        return Random.decidir(10) + especie.capacidadDeContagio()
    }

    fun serInfectadoCon(especie: Especie) {
        especies.add(especie)
    }

    fun estaSano(): Boolean {
        return especies.isEmpty()
    }

    fun tieneEfermedad(especieId: Long): Boolean {
        return especies.filter { e -> e.id == especieId }.isNotEmpty()
    }

    fun mover(ubicacion: Ubicacion) {
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
            return vector == Insecto
        }
    };
    /*
    fun esContagioExitoso(vectorAInfectar: Vector, especie: Especie): Boolean {
        return ubicacion.nombre == vectorAInfectar.ubicacion.nombre
                && vectorAInfectar.tipo.puedeSerInfectado(this)
                && Random.decidir(100) < porcentajeDeContagioExitoso(especie)
                && supresionNoExitosa(vectorAInfectar, especie)
    }
     */

    abstract fun puedeSerInfectado(vector: TipoDeVector): Boolean
}