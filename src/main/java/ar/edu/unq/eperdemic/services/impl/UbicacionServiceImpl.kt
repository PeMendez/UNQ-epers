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
class UbicacionServiceImpl(): UbicacionService {

    @Autowired private lateinit var neo4jUbicacionDAO: Neo4jUbicacionDAO
    @Autowired private lateinit var ubicacionDAO: UbicacionDAO
    @Autowired private lateinit var vectorServiceImpl: VectorServiceImpl
    @Autowired private lateinit var vectorDAO: VectorDAO

    override fun mover(vectorId: Long, ubicacionid: Long) {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionid)?: throw NoExisteElid("el id de la ubiacion no existe en la base de datos")
        val vector = vectorServiceImpl.recuperarVector(vectorId)
        val ubicacionNeo4JAMoverse = neo4jUbicacionDAO.findByIdRelacional(ubicacionid).get()
        val ubicacionNeo4JActual = neo4jUbicacionDAO.findByIdRelacional(vector.ubicacion.id!!).get()
        val caminoDeConexionEntreUbicaciones = neo4jUbicacionDAO.conectadosPorCamino(ubicacionNeo4JActual.nombre, ubicacionNeo4JAMoverse.nombre)

        if (!caminoDeConexionEntreUbicaciones.isPresent) {
            throw UbicacionMuyLejana("La ubicacion '" + ubicacionNeo4JAMoverse.nombre + "' es muy lejana para moverse.")
        } else if (vector.tipo.puedeMoverseACamino(caminoDeConexionEntreUbicaciones.get())) {
            intentarMover(vector, ubicacion)
        } else {
            throw UbicacionNoAlcanzable("El vector con ID '" + vectorId + "' no puede moverse a la ubicación '" + ubicacionNeo4JAMoverse.nombre + "'")
        }
    }

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

    override fun guardar(ubicacion: Ubicacion) {
        var flag = true
        try {
            ubicacionDAO.recuperarUbicacionPorNombre(ubicacion.nombre)
        } catch (e: EmptyResultDataAccessException) {
            flag = false
            ubicacionDAO.save(ubicacion)
        }
        if (flag) {
            throw NombreDeUbicacionRepetido("Ya existe una ubicacion con ese nombre.")
        }
    }

    fun conectarConQuery(ubicacionOrigen: String, ubicacionDestino:String, tipoDeCamino:String){
        existeUbicacionPorNombre(ubicacionOrigen)
        existeUbicacionPorNombre(ubicacionDestino)
        esTipoDeCaminoValido(tipoDeCamino)
        val ubiOrigen = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionOrigen).get()
        val ubiDestino = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionDestino).get()

        //comento porque no anduvo////////////////////////////////////////////////////////////
        //ubiOrigen.ubicaciones.add(ubiDestino)
        //neo4jUbicacionDAO.save(ubiOrigen)
        neo4jUbicacionDAO.conectar(ubiOrigen.idRelacional!!,ubiDestino.idRelacional!!, tipoDeCamino)
    }

    fun hayConexionDirecta(ubicacionOrigen: String, ubicacionDestino:String): Boolean{
        existeUbicacionPorNombre(ubicacionOrigen)
        existeUbicacionPorNombre(ubicacionDestino)
        val ubiOrigen = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionOrigen).get()
        val ubiDestino = neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacionDestino).get()

        return neo4jUbicacionDAO.hayConexionDirecta(ubiOrigen.idRelacional!!,ubiDestino.idRelacional!!)
    }

    fun conectados(ubicacionOrigen:String):List<UbicacionNeo4J>{
        existeUbicacionPorNombre(ubicacionOrigen)
        return neo4jUbicacionDAO.conectados(ubicacionOrigen)
    }

    private fun existeUbicacionPorNombre(nombreDeUbicacionABuscar:String){
        if (!neo4jUbicacionDAO.recuperarUbicacionPorNombre(nombreDeUbicacionABuscar).isPresent
            || ubicacionDAO.recuperarUbicacionPorNombre(nombreDeUbicacionABuscar).id == null) {
            throw NoExisteElNombreDeLaUbicacion("Ubicación no encontrada")
        }
    }

    //al final borrar lo que no se use
    private fun verificarCaminoValido(camino: String): String {
        val caminoUpper = camino.uppercase()
        if (caminoUpper == "AEREO" ||
            caminoUpper == "TERRESTRE" ||
            caminoUpper == "MARITIMO") {
            return caminoUpper
        } else {
            throw TipoDeCaminoInvalido("El tipo de camino '" + camino +  "' por el que se intenta conectar es inválido.")
        }
    }
    private fun esTipoDeCaminoValido(camino: String): Boolean {
        return TipoDeVector.values().map { t -> t.caminosCompatibles() }.toList().flatten().contains(camino)
    }

    fun caminosCompataibles(caminosCompataibles: List<String>, ubicacionOrigen:String,  ubicacionDestino: String):List<List<UbicacionNeo4J>>{
        return neo4jUbicacionDAO.caminosCompatibles(caminosCompataibles, ubicacionOrigen, ubicacionDestino).get()
    }

    private fun intentarMover(vector: Vector, ubicacion: Ubicacion) {
        if (vector.ubicacion.id!! != ubicacion.id!!) {
            vector.mover(ubicacion)
            val vectoresEnUbicacion = ubicacionDAO.recuperarVectores(ubicacion.id!!)
            vectorDAO.save(vector)
            if (!vector.estaSano()) {
                vectorServiceImpl.contagiar(vector, vectoresEnUbicacion)
            }
        }
    }
}

