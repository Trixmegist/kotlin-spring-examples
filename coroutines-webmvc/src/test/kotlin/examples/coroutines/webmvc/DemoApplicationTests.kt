package examples.coroutines.webmvc

import examples.coroutines.webmvc.service.Customer
import examples.coroutines.webmvc.service.CustomerRepository
import examples.coroutines.webmvc.service.DemoService
import examples.coroutines.webmvc.web.CustomerController
import examples.coroutines.webmvc.web.DemoController
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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kotlin.experimental.coroutine.event.CoroutineApplicationEventPublisher
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest
class DemoApplicationTests {

    @Test
    fun contextLoads() {
    }
}

@ExtendWith(MockKExtension::class)
@WebAppConfiguration
class CoTests {

    lateinit var mvc: MockMvc

    @MockK
    lateinit var demoService: DemoService
    @MockK
    lateinit var customerRepository: CustomerRepository
    @MockK
    lateinit var publisher: ApplicationEventPublisher
    @MockK
    lateinit var coroutinePublisher: CoroutineApplicationEventPublisher

    @InjectMockKs
    lateinit var demoController: DemoController
    @InjectMockKs
    lateinit var customerController: CustomerController

    @BeforeEach
    fun init() {
        mvc = MockMvcBuilders
                .standaloneSetup(demoController)
                .setCustomArgumentResolvers(ContinuationArgumentResolver)
                .setCustomReturnValueHandlers(DeferredReturnValueHandler)
                .build()
    }

    @Test
    @Disabled("As of now, MockK mocks don't work with MockMVC")
    fun `Test coroutine through mockMvc`() {
        coEvery { demoService.delayedReturn("Hello", 10) } returns "Hello"

        mvc.perform(MockMvcRequestBuilders.get("/delayed"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string("Hello"))

        coVerify { demoService.delayedReturn("Hello", 10) }
    }

    @Test
    fun `Test coroutine directly`() {
        val expectedCustomer = Customer(name = "Johnny Depp")
        coEvery { customerRepository.findByName("Depp") } returns expectedCustomer

        val result = runBlocking { customerController.customer("Depp") }

        assertEquals(expectedCustomer, result)
        coVerify { customerRepository.findByName("Depp") }
    }
}