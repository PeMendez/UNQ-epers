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
             var especies: MutableList<Especie>) {

    fun infectar(especie: Especie) {
        especies.add(especie)
    }


    fun esContagioExitoso(vector: Vector, especie: Especie): Boolean {
        return ubicacion.nombre == vector.ubicacion.nombre
                && tipo.puedeInfectar(vector.tipo)
                && (1..10).random() + especie.patogeno.capacidadDeContagio > vector.capacidadDeDefensa()
    }
    fun capacidadDeDefensa():Int{
        return especies.map { e -> e.patogeno.capacidadDeDefensa }.average().toInt()
        //falta ver que hacer si no esta infectado por ninguna especie
    }

}

enum class TipoDeVector {
    Persona,Insecto,Animal;

    fun puedeInfectar(vector: TipoDeVector): Boolean {
        return true
    }
}