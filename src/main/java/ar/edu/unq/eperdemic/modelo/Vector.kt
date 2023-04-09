package ar.edu.unq.eperdemic.modelo

class Vector( var id: Long?,
              var tipo: TipoDeVector,
              var ubicacion: Ubicacion,
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
    Persona {
        fun puedeInfectar(){

        }
    },
    Insecto{
        fun infectar(){

        }
    },
    Animal{
        fun infectar(){

        }
    };

    fun puedeInfectar(vector: TipoDeVector): Boolean {

        return TODO("Provide the return value")
    }
}