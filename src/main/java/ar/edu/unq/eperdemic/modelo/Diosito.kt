package ar.edu.unq.eperdemic.modelo

object Diosito {

    var esRandom : Boolean = true
    fun decidir(limiteFinal: Int) : Int {

        return if (esRandom){
                    (1..limiteFinal).random()
                } else {
                    limiteFinal
                }
    }

    fun switchModo(esRandom: Boolean) {
         this.esRandom = esRandom
    }
}