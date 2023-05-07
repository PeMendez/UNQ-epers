package ar.edu.unq.eperdemic.utils

import ar.edu.unq.eperdemic.modelo.Random
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
/*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDataDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
*/
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DataServiceSpring : DataService {

    @Autowired
    lateinit var ubicacionDAO: UbicacionDAO

    @Autowired
    lateinit var vectorDAO: VectorDAO

    @Autowired
    lateinit var patogenoDAO: PatogenoDAO

    /*
    val hibernateDao = HibernateDataDAO()

    val patogenoDAO = HibernatePatogenoDAO()
    val ubicacionDAO = HibernateUbicacionDAO()
    val vectorDAO = HibernateVectorDAO()
    val patogenoService = PatogenoServiceImpl(patogenoDAO)
    val ubicacionService = UbicacionServiceImpl(ubicacionDAO)
    val vectorService = VectorServiceImpl(vectorDAO)

    val patogeno1 = Patogeno("tipo1")
    val patogeno2 = Patogeno("tipo2")
    val patogeno3 = Patogeno("tipo3")
    val patogeno4 = Patogeno("tipo4")
    val ubicacion1 = Ubicacion("ubicacion1")
    val ubicacion2 = Ubicacion("ubicacion2")
    val ubicacion3 = Ubicacion("ubicacion3")



    */
    override fun crearSetDeDatosIniciales() {
        Random.switchModo(false)
    /*
        runTrx {
            Random.switchModo(false)
            val listaPatogenosCreados = crearPatogenos()
            val listaDeUbicacionesCreadas = crearUbicaciones()
            crearVectores(listaDeUbicacionesCreadas)

            crearEspecies(listaPatogenosCreados, listaDeUbicacionesCreadas)
            */
        }

        /*
    }
    private fun crearPatogenos(): List<Patogeno> {
        val patogeno1Creado = patogenoService.crearPatogeno(patogeno1)
        val patogeno2Creado = patogenoService.crearPatogeno(patogeno2)
        val patogeno3Creado = patogenoService.crearPatogeno(patogeno3)
        val patogeno4Creano = patogenoService.crearPatogeno(patogeno4)
        return listOf(patogeno1Creado, patogeno2Creado, patogeno3Creado, patogeno4Creano)
    }

    private fun crearUbicaciones(): List<Ubicacion> {
        val ubicacion1Creada = ubicacionService.crearUbicacion(ubicacion1.nombre)
        val ubicacion2Creada = ubicacionService.crearUbicacion(ubicacion2.nombre)
        val ubicacion3Creada = ubicacionService.crearUbicacion(ubicacion3.nombre)
        val ubicacion4Creada = ubicacionService.crearUbicacion("ubicacion4")
        val ubicacion5Creada = ubicacionService.crearUbicacion("ubicacion5")
        val ubicacion6Creada = ubicacionService.crearUbicacion("ubicacion6")
        val ubicacion7Creada = ubicacionService.crearUbicacion("ubicacion7")
        val ubicacion8Creada = ubicacionService.crearUbicacion("ubicacion8")
        val ubicacion9Creada = ubicacionService.crearUbicacion("ubicacion9")
        return listOf(ubicacion1Creada, ubicacion2Creada, ubicacion3Creada,
                      ubicacion4Creada, ubicacion5Creada, ubicacion6Creada,
                      ubicacion7Creada, ubicacion8Creada, ubicacion9Creada)
    }

    private fun crearVectores(listaDeUbicaciones: List<Ubicacion>) {
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Insecto, listaDeUbicaciones[1].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[6].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[7].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[1].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[6].id!!)

    }

    private fun crearEspecies(listaDePatogenos: List<Patogeno>, listaDeUbicaciones: List<Ubicacion>) {
        patogenoService.agregarEspecie(listaDePatogenos[0].id!!, "especie1", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie2", listaDeUbicaciones[1].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[2].id!!, "especie3", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie4", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie5", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[0].id!!, "especie6", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie7", listaDeUbicaciones[1].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[2].id!!, "especie8", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie9", listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie10", listaDeUbicaciones[2].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, "especie11", listaDeUbicaciones[2].id!!)

    }
    */


    @Transactional
    override fun eliminarTodo() {
        ubicacionDAO.deleteAll()
        vectorDAO.deleteAll()
        patogenoDAO.deleteAll()
    }

}