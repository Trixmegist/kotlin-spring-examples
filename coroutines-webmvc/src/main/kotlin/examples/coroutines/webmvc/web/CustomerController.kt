package examples.coroutines.webmvc.web

import examples.coroutines.webmvc.service.CustomerRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class CustomerController(val customerRepository: CustomerRepository) {
    @GetMapping("/customers")
    suspend fun customers() = customerRepository.findAll()

    @GetMapping("/customers/{name}")
    suspend fun customer(@PathVariable name: String) = customerRepository.findByName(name)
}
