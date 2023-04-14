package ar.edu.unq.eperdemic.modelo

object Diosito {

    var esRandom : Boolean = true
    fun decidir(limite: Int = 1) : Int {

        return if (esRandom){
                    (1..limite).random()
                } else {
                    limite
                }
    }

    fun switchModo() {
         esRandom = !esRandom
    }
}