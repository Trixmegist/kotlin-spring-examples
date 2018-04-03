package examples.coroutines.webmvc

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import examples.MockitoExtension
import examples.coroutines.webmvc.service.Customer
import examples.coroutines.webmvc.service.CustomerRepository
import examples.coroutines.webmvc.web.CustomerController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

val OBJECT_MAPPER = ObjectMapper()

@Throws(JsonProcessingException::class)
fun <T> T.asJson(): String = OBJECT_MAPPER.writeValueAsString(this)

@ExtendWith(SpringExtension::class)
@SpringBootTest
class DemoApplicationTests {
    @Test
    fun contextLoads() {
    }
}

@ExtendWith(MockKExtension::class)
class `MockK coroutines tests` {

    lateinit var mvc: MockMvc
    @MockK
    lateinit var customerRepository: CustomerRepository
    @InjectMockKs
    lateinit var customerController: CustomerController

    @BeforeEach
    fun init() {
        mvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setCustomArgumentResolvers(ContinuationArgumentResolver)
                .setCustomReturnValueHandlers(DeferredReturnValueHandler)
                .build()
    }

    @Test
    @Disabled("As of now, MockK doesn't work with coroutines through MockMVC")
    fun `Test coroutine through mockMvc`() {
        val expectedCustomer = Customer(name = "Johnny Depp")
        coEvery { customerRepository.findByName("Depp") } returns expectedCustomer

        mvc.perform(get("/delayed"))
                .andExpect(status().isOk)
                .andExpect(content().string("Hello"))

        coVerify { customerRepository.findByName("Depp") }
    }

    @Test
    fun `Test coroutine directly`() = runBlocking {
        val expectedCustomer = Customer(name = "Johnny Depp")
        coEvery { customerRepository.findByName("Depp") } returns expectedCustomer

        val result = customerController.customer("Depp")

        assertEquals(expectedCustomer, result)
        coVerify { customerRepository.findByName("Depp") }
    }
}

@ExtendWith(MockitoExtension::class)
class `Mockito corotines tests` {

    lateinit var mvc: MockMvc
    @Mock
    lateinit var customerRepository: CustomerRepository
    @InjectMocks
    lateinit var customerController: CustomerController

    @BeforeEach
    fun init() {
        mvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setCustomArgumentResolvers(ContinuationArgumentResolver)
                .setCustomReturnValueHandlers(DeferredReturnValueHandler)
                .build()
    }

    @Test
    fun `Test coroutine through mockMvc`() = runBlocking<Unit> {
        val expectedCustomer = Customer(name = "Johnny Depp")

        whenever(customerRepository.findByName("Depp")).thenReturn(expectedCustomer)

        mvc.perform(MockMvcRequestBuilders.get("/customers/Depp"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(expectedCustomer.asJson()))

        verify(customerRepository).findByName("Depp")
    }

    @Test
    fun `Test coroutine directly`() = runBlocking<Unit> {
        val expectedCustomer = Customer(name = "Johnny Depp")
        whenever(customerRepository.findByName("Depp")).thenReturn(expectedCustomer)

        val result = customerController.customer("Depp")

        assertEquals(expectedCustomer, result)
        verify(customerRepository).findByName("Depp")
    }
}
