package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Vector(var tipo: TipoDeVector,
             @ManyToOne
             var ubicacion: Ubicacion,
             @ManyToMany(fetch = FetchType.EAGER)
             var especies : MutableSet<Especie> = mutableSetOf(),
             ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToMany(fetch = FetchType.EAGER)
    var mutaciones: MutableSet<Mutacion> = mutableSetOf()

    fun intentarInfectarConEspecies(vectorAInfectar: Vector) {
        val especiesDelVector = ArrayList(especies)
        especiesDelVector.forEach { e ->
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
        if (especie.mutacionExitosa(this)) {
            val mutacionContraida = especie.mutar()
            this.agregarMutacion(mutacionContraida)
        }
    }


    private fun agregarMutacion(mutacion: Mutacion) {
        if (!this.mutaciones.any { m -> m.sonMismaMutacion(mutacion) && m.sonDeMismaEspecie(mutacion)}){
            mutacion.activarSupresionSiCorresponde(this)
            mutaciones.add(mutacion)
        }
    }

    fun esContagioExitoso(vectorAInfectar: Vector, especie: Especie): Boolean {
        return  ubicacion.nombre == vectorAInfectar.ubicacion.nombre
                && Random.decidir(100) <= porcentajeDeContagioExitoso(especie)
                && supresionNoExitosa(vectorAInfectar, especie)
                && hayContagioPorTipo(especie, vectorAInfectar)
    }

    fun hayContagioPorTipo(especie: Especie, vectorAInfectar: Vector): Boolean {
        val mutacion = this.mutaciones.find { m -> m.esBioalteracionGenetica() && m.especie.sonMismaEspecie(especie)}
        return if (mutacion != null) {
            (mutacion.tipoDeVector == vectorAInfectar.tipo) || vectorAInfectar.tipo.puedeSerInfectado(this.tipo)
        } else {
            vectorAInfectar.tipo.puedeSerInfectado(this.tipo)
        }
    }

    fun supresionNoExitosa(vectorAInfectar: Vector, especie: Especie): Boolean {
        var supresionNoExitosa = true
        vectorAInfectar.mutaciones.forEach { m ->
            if (m.tipoDeMutacion == TipoDeMutacion.SupresionBiomecanica) {
                supresionNoExitosa = supresionNoExitosa && m.potenciaDeMutacion!! <= especie.capacidadDeDefensa()
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

    fun tieneEnfermedad(especie: Especie): Boolean {
        return especies.any { e -> e.sonMismaEspecie(especie) }
    }

    fun mover(ubicacion: Ubicacion) {
        this.ubicacion = ubicacion
    }
    fun caminosCompatibles(): List<String>{
        return tipo.caminosCompatibles()
    }

    fun nombreDeUbicacionActual():String{
        return this.ubicacion.nombre
    }

}

enum class TipoDeVector {
    Persona {
        override fun puedeSerInfectado(vector: TipoDeVector): Boolean {
            return true
        }
        override fun caminosCompatibles(): List<String> {
            return listOf("TERRESTRE","MARITIMO")
        }
    },
    Insecto{
        override fun puedeSerInfectado(vector: TipoDeVector): Boolean {
            return this != vector
        }
        override fun caminosCompatibles(): List<String> {
            return listOf("TERRESTRE","AEREO")
        }
    },
    Animal{
        override fun puedeSerInfectado(vector: TipoDeVector): Boolean {
            return vector == Insecto
        }
        override fun caminosCompatibles(): List<String> {
            return listOf("TERRESTRE","MARITIMO","AEREO")
        }
    };

    abstract fun puedeSerInfectado(vector: TipoDeVector): Boolean
    abstract fun caminosCompatibles(): List<String>
}