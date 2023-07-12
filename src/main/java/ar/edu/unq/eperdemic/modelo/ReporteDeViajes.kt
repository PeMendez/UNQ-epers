package ar.edu.unq.eperdemic.modelo

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table
class ReporteDeViajes {

    @PrimaryKey
    var vector: Vector? = null
    var cantidadDeViajes: Int = 0
    var origen: Ubicacion? = null
    var destino: Ubicacion? = null
    var elementoMagico : String? = null
    var hechizo: String? = null 
}