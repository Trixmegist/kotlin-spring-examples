/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples.coroutines.webmvc.web

import examples.coroutines.webmvc.service.DemoService
import examples.coroutines.webmvc.util.logger
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kotlin.experimental.coroutine.annotation.Coroutine
import org.springframework.kotlin.experimental.coroutine.context.COMMON_POOL
import org.springframework.kotlin.experimental.coroutine.event.CoroutineApplicationEventPublisher
import org.springframework.kotlin.experimental.coroutine.web.client.CoroutineRestOperations
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class DemoController(
        private val demoService: DemoService,
        private val publisher: ApplicationEventPublisher,
        private val coroutinePublisher: CoroutineApplicationEventPublisher
) {
    private val restOperations = CoroutineRestOperations()

    @GetMapping("/delayed")
    suspend fun delayedReturn(): String {
        logger.info ("Before call to [demoService.delayed]")
        val result = demoService.delayedReturn("delayed", 1000)
        logger.info ("After call to [demoService.delayed]")

        return result
    }

    @GetMapping("/commmonPool")
    suspend fun commmonPoolReturn(): String {
        logger.info ("Before call to [demoService.commmonPoolReturn]")
        val result = demoService.commmonPoolReturn("commonPool")
        logger.info ("After call to [demoService.commmonPoolReturn]")

        return result
    }

    @GetMapping("/cachedDelayed")
    suspend fun cachedDelayedReturn(): String {
        logger.info ("Before call to [demoService.cachedDelayedReturn]")
        val result = demoService.cachedDelayedReturn("cachedDelayed", 500)
        logger.info ("After call to [demoService.cachedDelayedReturn]")

        return result
    }

    @GetMapping("/cachedCommonPoolDelayed")
    suspend fun cachedCommonPoolDelayed(): String {
        logger.info ("Before call to [demoService.cachedCommonPoolDelayedReturn]")
        val result = demoService.cachedCommonPoolDelayedReturn("cachedCommonPoolDelayed", 1500)
        logger.info ("After call to [demoService.cachedCommonPoolDelayedReturn]")

        return result
    }

    @GetMapping("/commonPoolController")
    @Coroutine(COMMON_POOL)
    suspend fun commonPoolController(): String {
        logger.info ("In [commonPoolController]")

        return "commonPoolController"
    }

    @GetMapping("/event")
    suspend fun event(): String {
        publisher.publishEvent(SimpleEvent("Hello"))
        publisher.publishEvent(DemoApplicationEvent(this, "Hello"))
        coroutinePublisher.publishEvent(SimpleEvent("Hello-coroutine"))
        coroutinePublisher.publishEvent(DemoApplicationEvent(this, "Hello-coroutine"))

        return "event"
    }

    @GetMapping("/rest")
    suspend fun rest(request: HttpServletRequest): String {
        logger.info ("Before call to [restOperations.getForEntity]")
        val url = request.requestURL.toString().replace("rest", "delayed")
        val result = restOperations.getForEntity(url, String::class.java)
        logger.info ("After call to [restOperations.getForEntity]")

        return "Rest result: ${result.body}"
    }

    override fun toString(): String = "DemoController"

    companion object {
        private val logger = logger()
    }
}

class DemoApplicationEvent(
    source: Any,
    val message: String
): ApplicationEvent(source) {

    override fun toString(): String = "DemoApplicationEvent(source=$source, message=$message)"
}

data class SimpleEvent(
    val message: String
)