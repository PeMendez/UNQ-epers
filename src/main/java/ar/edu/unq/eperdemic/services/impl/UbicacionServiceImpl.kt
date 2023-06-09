package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.*
import ar.edu.unq.eperdemic.persistencia.dao.Neo4jUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class UbicacionServiceImpl: UbicacionService {

    @Autowired private lateinit var neo4jUbicacionDAO: Neo4jUbicacionDAO
    @Autowired private lateinit var ubicacionDAO: UbicacionDAO
    @Autowired private lateinit var vectorServiceImpl: VectorServiceImpl
    @Autowired private lateinit var vectorDAO: VectorDAO

    override fun expandir(ubicacionId: Long) {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
        val vectores = ubicacionDAO.recuperarVectores(ubicacion.id!!)
        val vectoresInfectados = vectores.filter {  v -> !v.estaSano() }
        if (vectoresInfectados.isNotEmpty()) {
            val vectorAlAzar = vectoresInfectados[Random.decidir(vectoresInfectados.size)-1]
            vectorServiceImpl.contagiar(vectorAlAzar, vectores)
        }
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        if (neo4jUbicacionDAO.recuperarUbicacionPorNombre(nombreUbicacion).isPresent) {
            throw NombreDeUbicacionRepetido("Ya existe una ubicacion con ese nombre.")
        }
        try {
            ubicacionDAO.recuperarUbicacionPorNombre(nombreUbicacion)
        } catch (e: EmptyResultDataAccessException) {
            val nuevaUbicacion = Ubicacion(nombreUbicacion)
            ubicacionDAO.save(nuevaUbicacion)
            neo4jUbicacionDAO.save(nuevaUbicacion.aUbicacionNeo4J())
            return nuevaUbicacion
        }
        throw NombreDeUbicacionRepetido("Ya existe una ubicacion con ese nombre.")
    }

    override fun recuperarTodos(): List<Ubicacion> {
        return ubicacionDAO.findAll().toList()
    }

    override fun recuperarTodos(page: Pageable): Page<Ubicacion> {
        return ubicacionDAO.findAll(page)
    }


    override fun recuperar(ubicacionId: Long) : Ubicacion {
        return ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
    }

    override fun recuperarVectores(ubicacionId: Long): List<Vector> {
        return ubicacionDAO.recuperarVectores(ubicacionId)
    }

    override fun conectar(ubicacionOrigen: String, ubicacionDestino:String, tipoDeCamino:String){
        existeUbicacionPorNombre(ubicacionOrigen)
        existeUbicacionPorNombre(ubicacionDestino)
        val caminoVerificado = esTipoDeCaminoValido(tipoDeCamino)
        val ubiOrigen = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionOrigen).get()
        val ubiDestino = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionDestino).get()

        neo4jUbicacionDAO.conectar(ubiOrigen.idRelacional!!,ubiDestino.idRelacional!!, caminoVerificado)
    }

    fun hayConexionDirecta(ubicacionOrigen: String, ubicacionDestino:String): Boolean{
        existeUbicacionPorNombre(ubicacionOrigen)
        existeUbicacionPorNombre(ubicacionDestino)
        val ubiOrigen = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionOrigen).get()
        val ubiDestino = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionDestino).get()

        return neo4jUbicacionDAO.hayConexionDirecta(ubiOrigen.idRelacional!!,ubiDestino.idRelacional!!)
    }

    override fun conectados(ubicacionOrigen:String): List<Ubicacion>{
        existeUbicacionPorNombre(ubicacionOrigen)
        val ubicacionesConectadas =  neo4jUbicacionDAO.conectados(ubicacionOrigen)
        return ubicacionesConectadas.map { u -> recuperar(u.idRelacional!!) }
    }

    private fun existeUbicacionPorNombre(nombreDeUbicacionABuscar:String){
        if (!neo4jUbicacionDAO.recuperarUbicacionPorNombre(nombreDeUbicacionABuscar).isPresent
            || ubicacionDAO.recuperarUbicacionPorNombre(nombreDeUbicacionABuscar).id == null) {
            throw NoExisteElNombreDeLaUbicacion("La ubicación $nombreDeUbicacionABuscar no existe en la base de datos")
        }
    }

    private fun esTipoDeCaminoValido(camino: String): String {
        val caminoAVerificar = camino.uppercase()
        if (TipoDeVector.values().flatMap { t -> t.caminosCompatibles() }.contains(caminoAVerificar)) {
            return caminoAVerificar
        } else {
            throw TipoDeCaminoInvalido("El tipo de camino '$camino' no es válido.")
        }
    }

    override fun mover(vectorId: Long, ubicacionid: Long) {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionid)?: throw NoExisteElid("el id de la ubiacion no existe en la base de datos")
        val vector = vectorServiceImpl.recuperarVector(vectorId)
        if(vector.ubicacion.id!! == ubicacionid){
            throw EsMismaUbicacion("No podes moverte a la misma ubicacion en la que te encontras")
        }
        val ubicacionNeo4JAMoverse = neo4jUbicacionDAO.findByIdRelacional(ubicacionid).get()
        val ubicacionNeo4JActual = neo4jUbicacionDAO.findByIdRelacional(vector.ubicacion.id!!).get()
        val caminoDeConexionEntreUbicaciones = neo4jUbicacionDAO.conectadosPorCamino(ubicacionNeo4JActual.nombre, ubicacionNeo4JAMoverse.nombre)

        if (!caminoDeConexionEntreUbicaciones.isPresent) {
            throw UbicacionMuyLejana("La ubicacion '" + ubicacionNeo4JAMoverse.nombre + "' es muy lejana para moverse.")
        } else if (neo4jUbicacionDAO.puedeMoversePorCamino(vector.caminosCompatibles(),caminoDeConexionEntreUbicaciones.get())) {
            intentarMover(vector, ubicacion)
        } else {
            throw UbicacionNoAlcanzable("El tipo de vector " + vector.tipo + " no puede moverse por el tipo de camino " + caminoDeConexionEntreUbicaciones.get())
        }
    }
    private fun intentarMover(vector: Vector, ubicacion: Ubicacion) {
            vector.mover(ubicacion)
            val vectoresEnUbicacion = ubicacionDAO.recuperarVectores(ubicacion.id!!)
            vectorDAO.save(vector)
            if (!vector.estaSano()) {
                vectorServiceImpl.contagiar(vector, vectoresEnUbicacion)
            }
        }

    override fun moverMasCorto(vectorId:Long, nombreDeUbicacion:String){
        val vector = vectorDAO.findByIdOrNull(vectorId)?: throw NoExisteElid("No existe el ID del vector")
        val ubicacionDelVector = vector.nombreDeUbicacionActual()
        existeUbicacionPorNombre(nombreDeUbicacion)
        if(ubicacionDelVector == nombreDeUbicacion){
            throw EsMismaUbicacion("No podes moverte a la misma ubicacion en la que te encontras")
        }
        val caminosCompatibles = vector.caminosCompatibles()
        val caminoMasCorto = caminoMasCortoEntre(caminosCompatibles, ubicacionDelVector, nombreDeUbicacion)
        if (caminoMasCorto.isEmpty()){
            throw UbicacionNoAlcanzable("El vector no puede moverse a la ubicacion $nombreDeUbicacion. Ya que el camino o bien no es compatible o no existe una conexión posible.")
        } else {
            caminoMasCorto.forEach { ubicacion ->
                mover(vectorId, ubicacion.idRelacional!!)
            }
        }
    }
    private fun caminoMasCortoEntre(caminosCompatibles:List<String>, ubicacionDelVector: String, ubicacionDestino:String):List<UbicacionNeo4J> {
        return neo4jUbicacionDAO.caminoMasCortoEntre(caminosCompatibles, ubicacionDelVector, ubicacionDestino).get().drop(1)
    }
}

