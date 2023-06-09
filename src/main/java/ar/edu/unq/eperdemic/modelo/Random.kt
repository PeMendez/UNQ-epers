package ar.edu.unq.eperdemic.modelo

object Random {

    private var esRandom : Boolean = true
    fun decidir(limiteFinal: Int) : Int {

        return if (esRandom){
                    (1..limiteFinal).random()
                } else {
                    limiteFinal
                }
    }
    fun decidirTipoVector(): TipoDeVector{
        val tipos = TipoDeVector.values()
        return if (esRandom){
            tipos.get((0..2).random())
        } else {
            tipos.get(1)
        }
    }

    fun switchModo(esRandom: Boolean) {
         this.esRandom = esRandom
    }
}