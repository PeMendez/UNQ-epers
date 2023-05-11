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
    var mutaciones : MutableSet<Mutacion> = mutableSetOf()

    fun intentarInfectarConEspecies(especies: List<Especie>, vectorAInfectar:Vector) {
        especies.forEach { e ->
            this.intentarInfectar(vectorAInfectar, e)
        }
    }

    fun intentarInfectar(vectorAInfectar: Vector, especie: Especie) {
        if (esContagioExitoso(vectorAInfectar,especie)) {
            //vectorInfectado.mutar(especie)
            vectorAInfectar.serInfectadoCon(especie)
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
            this.eliminarEspeciesSegunMutacionSupresion(mutacion)
        }
        mutaciones.add(mutacion)
    }

    private fun eliminarEspeciesSegunMutacionSupresion(mutacion: Mutacion) {
        val especiesAEliminar = mutableListOf<Especie>()
        especies.forEach { e ->
            if (e.patogeno.capacidadDeDefensa < mutacion.potenciaDeMutacion!! && e.id!! != mutacion.especie.id!!) {
                especiesAEliminar.add(e)
            }
        }
        especiesAEliminar.forEach { e -> especies.remove(e)  }
    }

    fun esContagioExitoso(vectorAInfectar: Vector, especie: Especie): Boolean {
        var supresionNoExitosa = true
        vectorAInfectar.mutaciones.forEach { m ->
            if (m.tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
                supresionNoExitosa = supresionNoExitosa && m.potenciaDeMutacion!! < especie.capacidadDeDefensa()
            }
        }
        return ubicacion.nombre == vectorAInfectar.ubicacion.nombre
                && vectorAInfectar.tipo.puedeSerInfectado(tipo)
                && Random.decidir(100) < porcentajeDeContagioExitoso(especie)
                && supresionNoExitosa
    }

    fun porcentajeDeContagioExitoso(especie:Especie): Int{
        return Random.decidir(10) + especie.capacidadDeContagio()
    }

   fun serInfectadoCon(especie: Especie) {
       var supresionNoExitosa = true
       this.mutaciones.forEach { m ->
           if (m.tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
               supresionNoExitosa = supresionNoExitosa && m.potenciaDeMutacion!! < especie.capacidadDeDefensa()
           }
       }
       if (supresionNoExitosa) {
           especies.add(especie)
       }
   }

    private fun esMutacionExitosa(especie: Especie): Boolean {
        return Random.decidir(100) < especie.capacidadDeBiomecanizacion()
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
            return vector == Insecto
        }
    };
    abstract fun puedeSerInfectado(vector: TipoDeVector): Boolean
}