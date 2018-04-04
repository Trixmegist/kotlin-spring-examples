package examples.webmvc.example1

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    private val log = log()

    @Bean
    fun init(repository: CustomerRepository) = CommandLineRunner {
        repository.saveAll(listOf(
                Customer("Jack", "Bauer"),
                Customer("Chloe", "O'Brian"),
                Customer("Kim", "Bauer"),
                Customer("David", "Palmer"),
                Customer("Michelle", "Dessler")
        ))

        log.info("Customers found with findAll():")
        repository.findAll().forEach { log.info(it.toString()) }

        log.info("Customer found with findById(1L):")
        repository.findById(1L).ifPresent { log.info(it.toString()) }

        log.info("Customer found with findByLastName('Bauer'):")
        repository.findByLastName("Bauer").forEach { log.info(it.toString()) }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

inline fun <reified T> T.log() = LoggerFactory.getLogger(T::class.java)!!
