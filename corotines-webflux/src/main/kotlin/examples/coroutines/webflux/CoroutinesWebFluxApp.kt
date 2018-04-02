package examples.coroutines.webflux

@SpringBootApplication
@EnableCoroutine
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
                                .flatMap { mono { customerRepository.saveAll(it) } }
                                .flatMapMany { Flux.fromIterable(it) }
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

                        "/customers".nest {
                            GET("/") {
                                ServerResponse.ok().body(flux { customerRepository.findAll() })
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