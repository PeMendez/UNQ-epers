package ar.edu.unq.eperdemic.spring.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("ar.edu.unq.eperdemic.persistencia.dao")
class AppConfiguration {

    @Bean
    fun groupName() : String {
        return "La Pelu"
    }

}

