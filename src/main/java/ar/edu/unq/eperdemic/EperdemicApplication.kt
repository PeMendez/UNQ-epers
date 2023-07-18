package ar.edu.unq.eperdemic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class EperdemicApplication

fun main(args: Array<String>) {
	runApplication<EperdemicApplication>(*args)
}
