package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Vector(var tipo: TipoDeVector,
             @ManyToOne
             var ubicacion: Ubicacion,
             @ManyToMany(fetch = FetchType.EAGER)
             var especies : MutableSet<Especie> = mutableSetOf())
            //@ManyToMany(fetch = FetchType.EAGER)
//          var mutaciones : MutableSet<Mutacion> = mutableSetOf())
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun intentarInfectarConEspecies(vectorAInfectar:Vector) {
        this.especies.forEach { e ->
            this.intentarInfectar(vectorAInfectar, e)
        }
    }
    fun intentarInfectar(vectorAInfectar: Vector, especie: Especie) {
        if (esContagioExitoso(vectorAInfectar,especie)) {
            //vectorInfectado.mutar(especie)
            vectorAInfectar.serInfectadoCon(especie)
        }
    }
    fun esContagioExitoso(vectorAInfectar: Vector, especie: Especie): Boolean {
        return ubicacion.nombre == vectorAInfectar.ubicacion.nombre
                && vectorAInfectar.tipo.puedeSerInfectado(tipo)
                && Random.decidir(100) < porcentajeDeContagioExitoso(especie)
    }
    fun porcentajeDeContagioExitoso(especie:Especie): Int{
        return Random.decidir(10) + especie.capacidadDeContagio()
    }
   fun serInfectadoCon(especie: Especie) {
       especies.add(especie)
   }
   private fun mutar(especie: Especie) : Boolean {
       if (esMutacionExitosa(especie)){
       //mutaciones.add()
       }
       return Random.decidir(100) < especie.capacidadDeBiomecanizacion()
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