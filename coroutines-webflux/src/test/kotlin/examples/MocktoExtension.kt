package examples


class MockitoExtension : TestInstancePostProcessor, ParameterResolver {

    override fun postProcessTestInstance(testInstance: Any,
                                         context: ExtensionContext) {
        MockitoAnnotations.initMocks(testInstance)
    }

    override fun supportsParameter(parameterContext: ParameterContext,
                                   extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.isAnnotationPresent(Mock::class.java)
    }

    override fun resolveParameter(parameterContext: ParameterContext,
                                  extensionContext: ExtensionContext): Any {
        return getMock(parameterContext.parameter, extensionContext)
    }

    private fun getMock(
            parameter: java.lang.reflect.Parameter, extensionContext: ExtensionContext): Any {

        val mockType = parameter.getType()
        val mocks = extensionContext.getStore(ExtensionContext.Namespace.create(
                MockitoExtension::class.java, mockType))
        val mockName = getMockName(parameter)

        return if (mockName != null) {
            mocks.getOrComputeIfAbsent(
                    mockName) { key -> mock(mockType, mockName) }
        } else {
            mocks.getOrComputeIfAbsent(
                    mockType.getCanonicalName()) { key -> mock(mockType) }
        }
    }

    private fun getMockName(parameter: java.lang.reflect.Parameter): String? {
        val explicitMockName = parameter.getAnnotation(Mock::class.java)
                .name.trim()
        if (!explicitMockName.isEmpty()) {
            return explicitMockName
        } else if (parameter.isNamePresent()) {
            return parameter.getName()
        }
        return null
    }
}