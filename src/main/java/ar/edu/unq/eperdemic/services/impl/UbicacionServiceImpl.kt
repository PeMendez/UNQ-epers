package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.*
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.services.UbicacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
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
    @Autowired private lateinit var mongoUbicacionDAO: MongoUbicacionDAO
    @Autowired private lateinit var distritoDAO: DistritoDAO

    override fun expandir(ubicacionId: Long) {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionId)?: throw NoExisteElid("el id buscado no existe en la base de datos")
        val vectores = ubicacionDAO.recuperarVectores(ubicacion.id!!)
        val vectoresInfectados = vectores.filter {  v -> !v.estaSano() }
        if (vectoresInfectados.isNotEmpty()) {
            val vectorAlAzar = vectoresInfectados[Random.decidir(vectoresInfectados.size)-1]
            vectorServiceImpl.contagiar(vectorAlAzar, vectores)
        }
    }

    override fun crearUbicacion(nombreUbicacion: String, coordenada: GeoJsonPoint): Ubicacion {
        try {
            ubicacionDAO.recuperarUbicacionPorNombre(nombreUbicacion)
            throw NombreDeUbicacionRepetido("Ya existe una ubicacion con ese nombre.")
        } catch (e: EmptyResultDataAccessException) {
            if (mongoUbicacionDAO.recuperarPorCoordenada(coordenada).isPresent) {
                throw CoordenadaInvalida("Ya existe una ubicación en la coordenada $coordenada")
            }
            val nuevaUbicacion = Ubicacion(nombreUbicacion)
            ubicacionDAO.save(nuevaUbicacion)
            neo4jUbicacionDAO.save(nuevaUbicacion.aUbicacionNeo4J())
            val coordenadas2 = coordenada.coordinates
            val distritoNombre = distritoDAO.distritoEnCoordenada(coordenadas2)
            if(distritoNombre.isPresent){
                val distritoNombreGet = distritoNombre.get().nombre
                mongoUbicacionDAO.save(nuevaUbicacion.aUbicacionMongo(coordenada, distritoNombreGet))
            } else {
                mongoUbicacionDAO.save(nuevaUbicacion.aUbicacionMongoSinNombre(coordenada))
            }

            return nuevaUbicacion
        }
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

    override fun recuperarUbicacionPorNombre(nombreUbicacion: String) : Ubicacion {
        return ubicacionDAO.recuperarUbicacionPorNombre(nombreUbicacion)?: throw NoExisteElNombreDeLaUbicacion("El nombre no existe en la base de datos")
    }

    fun recuperarUbicacionPorNombreSiExiste(ubicacion:String):UbicacionNeo4J{
        return neo4jUbicacionDAO.recuperarUbicacionPorNombre(ubicacion).orElseThrow{
            NoExisteElNombreDeLaUbicacion("No existe la ubicacion $ubicacion")
        }
    }

    override fun recuperarUbicacionNeoPorId(idUbicacion:Long) : UbicacionNeo4J {
        return neo4jUbicacionDAO.findByIdRelacional(idUbicacion).orElseThrow{
            NoExisteElid("No existe el id $idUbicacion")
        }
    }

    override fun recuperarUbicacionMongoPorId(idUbicacion: Long): UbicacionMongo {
        return mongoUbicacionDAO.findByIdRelacional(idUbicacion).orElseThrow {
            NoExisteElid("No existe el id $idUbicacion")
        }
    }

    override fun recuperarVectores(ubicacionId: Long): List<Vector> {
        return ubicacionDAO.recuperarVectores(ubicacionId)
    }

    override fun conectar(ubicacionOrigen: String, ubicacionDestino:String, tipoDeCamino:String){
        val caminoVerificado = esTipoDeCaminoValido(tipoDeCamino)
        val ubiOrigen = recuperarUbicacionPorNombreSiExiste(ubicacionOrigen)
        val ubiDestino = recuperarUbicacionPorNombreSiExiste(ubicacionDestino)

        neo4jUbicacionDAO.conectar(ubiOrigen.idRelacional!!,ubiDestino.idRelacional!!, caminoVerificado)
    }

    fun hayConexionDirecta(ubicacionOrigen: String, ubicacionDestino:String): Boolean{
        val ubiOrigen = recuperarUbicacionPorNombreSiExiste(ubicacionOrigen)
        val ubiDestino = recuperarUbicacionPorNombreSiExiste(ubicacionDestino)

        return neo4jUbicacionDAO.hayConexionDirecta(ubiOrigen.idRelacional!!,ubiDestino.idRelacional!!)
    }

    override fun conectados(ubicacionOrigen:String): List<Ubicacion>{
        recuperarUbicacionPorNombreSiExiste(ubicacionOrigen)
        val ubicacionesConectadas =  neo4jUbicacionDAO.conectados(ubicacionOrigen)
        return ubicacionesConectadas.map { u -> recuperar(u.idRelacional!!) }
    }

    private fun esTipoDeCaminoValido(camino: String): String {
        val caminoAVerificar = camino.uppercase()
        val caminosDisponibles = listOf<String>("TERRESTRE","MARITIMO","AEREO")
        if (caminosDisponibles.contains(caminoAVerificar)) {
            return caminoAVerificar
        } else {
            throw TipoDeCaminoInvalido("El tipo de camino '$camino' no es válido.")
        }
    }

    override fun mover(vectorId: Long, ubicacionid: Long) {
        val ubicacion = ubicacionDAO.findByIdOrNull(ubicacionid)?: throw NoExisteElid("el id de la ubiacion no existe en la base de datos")
        val vector = vectorServiceImpl.recuperarVector(vectorId)
        if (vector.esMago){
            vector.mover(ubicacion)
            //aca crear el registro en cassandra. 
        } else{
        if(vector.ubicacion.id!! == ubicacionid){
            throw EsMismaUbicacion("No podes moverte a la misma ubicacion en la que te encontras")
        }
        verificarDistanciaAlcanzable(ubicacionid, vector.ubicacion.id!!)
        val caminoDeConexionEntreUbicaciones = conectadosPorCamino(vector.nombreDeUbicacionActual(), ubicacion.nombre)
        if (neo4jUbicacionDAO.puedeMoversePorCamino(vector.caminosCompatibles(),caminoDeConexionEntreUbicaciones)) {
            intentarMover(vector, ubicacion)
        } else {
            throw UbicacionNoAlcanzable("El tipo de vector " + vector.tipo + " no puede moverse por el tipo de camino " + caminoDeConexionEntreUbicaciones)
        }
        }
    }

    private fun conectadosPorCamino(ubicacionNeoActual:String, ubicacionNeoAMover:String):String {
        return neo4jUbicacionDAO.conectadosPorCamino(ubicacionNeoActual, ubicacionNeoAMover).orElseThrow {
            UbicacionMuyLejana("La ubicacion '$ubicacionNeoAMover' es muy lejana para moverse.")
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
        recuperarUbicacionPorNombreSiExiste(nombreDeUbicacion)
        if(ubicacionDelVector == nombreDeUbicacion){
            throw EsMismaUbicacion("No podes moverte a la misma ubicacion en la que te encontras")
        }
        val caminosCompatibles = vector.caminosCompatibles()
        val caminoMasCorto = caminoMasCortoEntre(caminosCompatibles, ubicacionDelVector, nombreDeUbicacion)
        if (caminoMasCorto.isEmpty()){
            throw UbicacionNoAlcanzable("El vector no puede moverse a la ubicacion $nombreDeUbicacion. Ya que el camino o bien no es compatible o no existe una conexión posible.")
        } else {
            val caminosMasCortoUbicaciones = mutableListOf<Ubicacion>()
            caminoMasCorto.forEach { u ->
                caminosMasCortoUbicaciones.add(recuperar(u.idRelacional!!))
                verificarDistanciaAlcanzable(u.idRelacional!!, vector.ubicacion.id!!)
            }
            caminosMasCortoUbicaciones.forEach { ubicacion ->
                intentarMover(vector, ubicacion)
            }
        }
    }

    private fun verificarDistanciaAlcanzable(ubicacionIdAMover: Long, ubicacionIdActual: Long) {
        val ubicacionMongo = mongoUbicacionDAO.findByIdRelacional(ubicacionIdActual).get()
        if(!seEncuentraADistanciaAlcanzable(ubicacionMongo.coordenada.x, ubicacionMongo.coordenada.y, ubicacionIdAMover)) {
            throw UbicacionMuyLejana("La ubicacion se encuentra a más de 100km de distancia")
        }
    }

    private fun caminoMasCortoEntre(caminosCompatibles:List<String>, ubicacionDelVector: String, ubicacionDestino:String):List<UbicacionNeo4J> {
        return neo4jUbicacionDAO.caminoMasCortoEntre(caminosCompatibles, ubicacionDelVector, ubicacionDestino).get().drop(1)
    }

    fun seEncuentraADistanciaAlcanzable(longitude: Double, latitude: Double, idRelacionalAMoverse: Long): Boolean{
        return mongoUbicacionDAO.seEncuentraADistanciaAlcanzable(longitude, latitude, idRelacionalAMoverse).isPresent
    }

    fun idsDeUbicacionesEnfermas(): List<Long> {
        return ubicacionDAO.idsDeUbicacionesEnfermas()
    }

}

