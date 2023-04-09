package ar.edu.unq.eperdemic.modelo

class Vector( var id: Long?,
              var tipo: TipoDeVector,
              var ubicacion: Ubicacion,
              var especies: MutableList<Especie>) {
    fun contagiar(vectores: List<Vector>){ //me parece que esta funcion no va a nivel modelo
        vectores.forEach{v -> especies.forEach{e -> v.infectar(this,e)}}
    }


    private fun infectar(vector: Vector, especie: Especie) {
        if (this.ubicacion == vector.ubicacion && tipo.puedeInfectar(vector.tipo)){
            this.especies.add(especie)
        }
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