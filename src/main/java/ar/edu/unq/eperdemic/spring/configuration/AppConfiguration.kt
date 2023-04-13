package ar.edu.unq.eperdemic.spring.configuration

import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun groupName() : String {
        val groupName :String?  = System.getenv()["GROUP_NAME"]
        return groupName!!
    }

    @Bean
    fun patogenoDAO(): PatogenoDAO {
        return HibernatePatogenoDAO()
    }

    @Bean
    fun patogenoService(patogenoDAO: PatogenoDAO): PatogenoService {
        return PatogenoServiceImpl(patogenoDAO)
    }

}
