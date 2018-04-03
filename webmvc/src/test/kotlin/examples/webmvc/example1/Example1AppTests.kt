package examples.webmvc.example1

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.whenever
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import com.nhaarman.mockito_kotlin.verify as mockitoVerify
import io.mockk.verify as mockKVerify

private val OBJECT_MAPPER = ObjectMapper()

@Throws(JsonProcessingException::class)
private fun <T> T.asJson(): String = OBJECT_MAPPER.writeValueAsString(this)

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DataJpaTest
class Example1Tests {

    @Test
    fun contextLoads() {
    }
}

class MockitoKotlinMvcTest {

    lateinit var mvc: MockMvc

    @Mock
    lateinit var customerRepository: CustomerRepository

    @InjectMocks
    lateinit var customerController: CustomerController

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        mvc = MockMvcBuilders.standaloneSetup(customerController).build()
    }

    @Test
    fun `Should return`() {
        val expectedCustomer = Customer("Johnny", "Depp")

        whenever(customerRepository.findByLastName("Depp"))
                .thenReturn(listOf(expectedCustomer))

        mvc.perform(get("/customers/Depp"))
                .andExpect(status().isOk)
                .andExpect(content().string(listOf(expectedCustomer).asJson()))

        mockitoVerify(customerRepository).findByLastName("Depp")
    }

    @Test
    fun `Should throw`() {
        whenever(customerRepository.findByLastName("Depp"))
                .thenThrow(IllegalArgumentException::class.java)

        mvc.perform(get("/customers/Depp"))
                .andExpect(status().is5xxServerError)

        mockitoVerify(customerRepository).findByLastName("Depp")
    }
}

@ExtendWith(MockKExtension::class)
class MockKMvcTest {

    lateinit var mvc: MockMvc

    @MockK
    lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    lateinit var customerController: CustomerController

    @BeforeEach
    fun init() {
        mvc = MockMvcBuilders.standaloneSetup(customerController).build()
    }

    @Test
    fun `Should return`() {
        val expectedCustomer = Customer("Johnny", "Depp")

        every { customerRepository.findByLastName("Depp") } returns listOf(expectedCustomer)

        mvc.perform(get("/customers/Depp"))
                .andExpect(status().isOk)
                .andExpect(content().string(listOf(expectedCustomer).asJson()))

        mockKVerify { customerRepository.findByLastName("Depp") }
    }

    @Test
    fun `Should throw`() {
        every { customerRepository.findByLastName("Depp") } throws IllegalArgumentException()

        mvc.perform(get("/customers/Depp"))
                .andExpect(status().is5xxServerError)

        mockKVerify { customerRepository.findByLastName("Depp") }
    }
}