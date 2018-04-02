package examples.coroutines.webmvc.service

import kotlinx.coroutines.experimental.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.CoroutineMongoRepository
import org.springframework.stereotype.Component

interface CustomerRepository : CoroutineMongoRepository<Customer, String> {
    suspend fun findByName(name: String): Customer
}

@Document
data class Customer(val id: String? = null, val name: String? = null)

@Component
class DbInitializer(private val customerRepository: CustomerRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val customers = arrayOf("Zhenya", "Dima", "Stas", "Tair").map { Customer(name = it) }
        runBlocking { customerRepository.saveAll(customers) }.forEach { println(it) }
    }
}