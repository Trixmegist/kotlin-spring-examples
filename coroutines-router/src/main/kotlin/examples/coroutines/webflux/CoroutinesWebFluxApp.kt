package examples.coroutines.webflux

import kotlinx.coroutines.experimental.reactor.mono
import kotlinx.coroutines.experimental.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.CoroutineMongoRepository
import org.springframework.data.mongodb.repository.config.EnableCoroutineMongoRepositories
import org.springframework.kotlin.experimental.coroutine.EnableCoroutine
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux

@SpringBootApplication
@EnableCoroutine
@EnableCoroutineMongoRepositories
class CoroutinesWebFluxApp

fun main(args: Array<String>) {

    SpringApplicationBuilder()
            .sources(CoroutinesWebFluxApp::class.java)
            .initializers(beans {

                bean {
                    ApplicationRunner {
                        val customerRepository = ref<CustomerRepository>()
                        arrayOf("Zhenya", "Dima", "Stas", "Tair").toFlux()
                                .map { Customer(name = it) }
                                .collectList()
                                .map { customers ->
                                    runBlocking {
                                        customerRepository.saveAll(customers)
                                    }
                                }
                                .flatMapMany { Flux.fromIterable(it) }
                                .subscribe({ println(it) })
                    }
                }

                bean {
                    router {
                        val customerRepository = ref<CustomerRepository>()
                        "/customers".nest {
                            GET("/") {
                                ServerResponse.ok().body(mono { customerRepository.findAll() })
                            }
                            GET("/{name}") {
                                val name = it.pathVariable("name")
                                ServerResponse.ok().body(mono { customerRepository.findByName(name) })
                            }
                        }
                    }
                }
            })
            .run(*args)
}

interface CustomerRepository : CoroutineMongoRepository<Customer, String> {
    suspend fun findByName(name: String): Customer
}

@Document
data class Customer(val id: String? = null, val name: String? = null)