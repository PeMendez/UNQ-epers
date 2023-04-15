package ar.edu.unq.eperdemic.utils

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class DataServiceHibernate : DataService {

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
    val especie1 = Especie(patogeno1, "especie1", "pais1")
    val especie2 = Especie(patogeno2, "especie2", "pais2")
    val especie3 = Especie(patogeno3, "especie3", "pais3")
    val ubicacion1 = Ubicacion("ubicacion1")
    val ubicacion2 = Ubicacion("ubicacion2")
    val ubicacion3 = Ubicacion("ubicacion3")


    override fun crearSetDeDatosIniciales() {
        TransactionRunner.runTrx {
            val listaPatogenosCreados = crearPatogenos()
            val listaDeUbicacionesCreadas = crearUbicaciones()
            crearVectores(listaDeUbicacionesCreadas)

            crearEspecies(listaPatogenosCreados, listaDeUbicacionesCreadas)
        }
    }

    private fun crearPatogenos(): List<Patogeno> {
        val patogeno1Creado = patogenoService.crearPatogeno(patogeno1)
        val patogeno2Creado = patogenoService.crearPatogeno(patogeno2)
        val patogeno3Creado = patogenoService.crearPatogeno(patogeno3)
        return listOf(patogeno1Creado, patogeno2Creado, patogeno3Creado)
    }

    private fun crearUbicaciones(): List<Ubicacion> {
        val ubicacion1Creada = ubicacionService.crearUbicacion(ubicacion1.nombre)
        val ubicacion2Creada = ubicacionService.crearUbicacion(ubicacion2.nombre)
        val ubicacion3Creada = ubicacionService.crearUbicacion(ubicacion3.nombre)
        return listOf(ubicacion1Creada, ubicacion2Creada, ubicacion3Creada)
    }

    private fun crearEspecies(listaDePatogenos: List<Patogeno>, listaDeUbicaciones: List<Ubicacion>) {
        patogenoService.agregarEspecie(listaDePatogenos[0].id!!, especie1.nombre, listaDeUbicaciones[0].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[1].id!!, especie2.nombre, listaDeUbicaciones[1].id!!)
        patogenoService.agregarEspecie(listaDePatogenos[2].id!!, especie3.nombre, listaDeUbicaciones[2].id!!)
    }

    private fun crearVectores(listaDeUbicaciones: List<Ubicacion>) {
        vectorService.crearVector(TipoDeVector.Animal, listaDeUbicaciones[0].id!!)
        vectorService.crearVector(TipoDeVector.Insecto, listaDeUbicaciones[1].id!!)
        vectorService.crearVector(TipoDeVector.Persona, listaDeUbicaciones[2].id!!)
    }

    override fun eliminarTodo() {
        TransactionRunner.runTrx {
            hibernateDao.clear()
        }
    }

}