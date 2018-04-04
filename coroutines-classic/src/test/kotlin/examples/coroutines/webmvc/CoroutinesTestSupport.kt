package examples.coroutines.webmvc

import org.springframework.core.MethodParameter
import org.springframework.kotlin.experimental.coroutine.isSuspend
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED

object ContinuationArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
            parameter.method!!.isSuspend && isContinuationClass(parameter.parameterType)

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?) =
            object : Continuation<Any> {
                val deferredResult = DeferredResult<Any>()

                override val context: CoroutineContext
                    get() = EmptyCoroutineContext

                override fun resume(value: Any) {
                    deferredResult.setResult(value)
                }

                override fun resumeWithException(exception: Throwable) {
                    deferredResult.setErrorResult(exception)
                }
            }.apply {
                mavContainer!!.model[DEFERRED_RESULT] = deferredResult
            }
}

object DeferredReturnValueHandler : AsyncHandlerMethodReturnValueHandler {
    private val delegate = DeferredResultMethodReturnValueHandler()

    override fun supportsReturnType(returnType: MethodParameter): Boolean =
            returnType.method!!.isSuspend

    override fun handleReturnValue(returnValue: Any?, type: MethodParameter,
                                   mavContainer: ModelAndViewContainer, webRequest: NativeWebRequest) {
        val result = mavContainer.model[DEFERRED_RESULT] as DeferredResult<*>

        return delegate.handleReturnValue(result, type, mavContainer, webRequest)
    }

    override fun isAsyncReturnValue(returnValue: Any?, returnType: MethodParameter): Boolean =
            returnValue === COROUTINE_SUSPENDED
}

private fun <T> isContinuationClass(clazz: Class<T>) = Continuation::class.java.isAssignableFrom(clazz)

private const val DEFERRED_RESULT = "deferred_result"