package examples.coroutines.webmvc.service

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.CoroutineMongoRepository

interface CustomerRepository : CoroutineMongoRepository<Customer, String> {
    suspend fun findByName(name: String): Customer
}

@Document
data class Customer(val id: String? = null, val name: String? = null)