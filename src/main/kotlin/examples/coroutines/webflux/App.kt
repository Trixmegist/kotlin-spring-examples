package examples.coroutines.webflux

import examples.coroutines.webmvc.ApplicationConfiguration
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.kotlin.experimental.coroutine.EnableCoroutine
import reactor.core.publisher.Mono

@SpringBootApplication
@EnableCoroutine
class ApplicationConfiguration

fun main(args: Array<String>) {
    runApplication<ApplicationConfiguration>(*args)
}
//
//class DbInitizlizer(private val customerRepository: CustomerRepository) : ApplicationRunner {
//    override fun run(args: ApplicationArguments?) {
////        arrayOf("Zhenya", "Dima", "Stas", "Tair").toFlux()
////                .map { examples.webflux.Customer(name = it) }
////                .collectList()
////                .map { qwe -> customerRepository.saveAll(qwe) }
////                .subscribe { println(it) }
//    }
//}
//
//interface CustomerRepository : ReactiveMongoRepository<Customer, String> {
//    fun findByName(name: String): Mono<Customer>
//}
//
//@Document
//data class Customer(val id: String? = null, val name: String? = null)