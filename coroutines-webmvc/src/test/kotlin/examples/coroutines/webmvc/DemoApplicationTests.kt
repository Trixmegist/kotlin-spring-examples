package examples.coroutines.webmvc

import examples.coroutines.webmvc.service.DemoService
import examples.coroutines.webmvc.web.DemoController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
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
    lateinit var demoservice: DemoService
    @MockK
    lateinit var publisher: ApplicationEventPublisher;
    @MockK
    lateinit var coroutinePublisher: CoroutineApplicationEventPublisher

    @InjectMockKs
    lateinit var demoController: DemoController

    @BeforeEach
    fun init() {
        mvc = MockMvcBuilders.standaloneSetup(demoController).build()
    }

    @Test
    fun `Should return`() {
//        val expectedCustomer = Customer("Johnny", "Depp")

        coEvery { demoservice.delayedReturn("Hello", 10) } returns "Hello"

        mvc.perform(MockMvcRequestBuilders.get("/delayed"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string("Hello"))

        coVerify { demoservice.delayedReturn("Hello", 10) }
    }

//    @Test
//    fun `Should throw`() {
//        every { customerRepository.findByLastName("Depp") } throws IllegalArgumentException()
//
//        mvc.perform(get("/customers/Depp"))
//                .andExpect(status().is5xxServerError)
//
//        mockKVerify { customerRepository.findByLastName("Depp") }
//    }
}