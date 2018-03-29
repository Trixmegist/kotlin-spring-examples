package examples.reactive

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono

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
                            ServerResponse.ok().body(Flux.just("Hello, functional reactive"), String::class.java)
                        }
                    }
                }

                bean {
                    router {
                        val customerRepository = ref<CustomerRepository>()

                        GET("/customers") {
                            ServerResponse.ok().body( customerRepository.findAll())
                        }
                        GET("/customers/{name}") {
                            val name = it.pathVariable("name")
                            ServerResponse.ok().body(customerRepository.findByName(name))
                        }
                    }
                }
            })
            .run(*args)
}

//@RestController
//class CustomerController(val customerRepository: CustomerRepository) {
//    @GetMapping("/customers")
//    fun customers() = customerRepository.findAll()
//
//    @GetMapping("/customers/{name}")
//    fun customer(@PathVariable name: String) = customerRepository.findByName(name)
//}

interface CustomerRepository : ReactiveMongoRepository<Customer, String> {
    fun findByName(name: String): Mono<Customer>
}

@Document
data class Customer(val id: String? = null, val name: String? = null)