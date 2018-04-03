package examples.webflux

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux

@SpringBootApplication
class ReactiveApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(ReactiveApplication::class.java)
            .initializers(beans {

                bean {
                    ApplicationRunner {
                        val customerRepository = ref<CustomerRepository>()
                        arrayOf("Zhenya", "Dima", "Stas", "Tair").toFlux()
                                .map { Customer(name = it) }
                                .collectList()
                                .flatMapMany { customerRepository.saveAll(it) }
                                .subscribe { println(it) }
                    }
                }

                bean {
                    router {
                        GET("/hi") {
                            ServerResponse.ok().body(Flux.just("Hi from router"), String::class.java)
                        }
                    }
                }
            })
            .run(*args)
}

@RestController
class CustomerController(val customerRepository: CustomerRepository) {
    @GetMapping("/customers")
    fun customers() = customerRepository.findAll()

    @GetMapping("/customers/{name}")
    fun customer(@PathVariable name: String) = customerRepository.findByName(name)
}

interface CustomerRepository : ReactiveMongoRepository<Customer, String> {
    fun findByName(name: String): Mono<Customer>
}

@Document
data class Customer(val id: String? = null, val name: String? = null)