package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface EspecieDAO : JpaRepository<Especie, Long>  {


    @Query("select e from Vector v join v.especies e where v.tipo in (ar.edu.unq.eperdemic.modelo.TipoDeVector.Persona, ar.edu.unq.eperdemic.modelo.TipoDeVector.Animal) group by e order by count(v) desc")
    fun lideres(): List<Especie>

    @Query("select count(e.id) from Vector v join v.especies e where e.id = :especieId")
    fun cantidadDeInfectados(especieId: Long ): Int

    @Query("select e from Vector v join v.especies e where v.tipo in (ar.edu.unq.eperdemic.modelo.TipoDeVector.Persona) group by e order by count(v) desc")
    fun especieLider(): List<Especie>

    @Query("select e from Vector v join v.especies e where v.ubicacion.id = :ubicacionId group by e order by count(v) desc")
    fun especieLiderDeUbicacion(ubicacionId: Long) : List<Especie>

}