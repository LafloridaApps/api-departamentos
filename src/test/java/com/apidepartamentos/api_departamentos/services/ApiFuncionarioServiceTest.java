package com.apidepartamentos.api_departamentos.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Map;

import com.apidepartamentos.api_departamentos.config.ApiProperties;

class ApiFuncionarioServiceTest {
    private String resolveUrl(Map<String, Object> variables) throws Exception {
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().remove(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
        environment.getPropertySources().remove(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        environment.getPropertySources().addFirst(new MapPropertySource("test", variables));
        String template = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"))
                .getProperty("api.funcionario-url");
        return environment.resolveRequiredPlaceholders(template);
    }

    @Test void usaLocalhostSinVariableFuncionario() throws Exception {
        String url = resolveUrl(Map.of());
        assertEquals("http://localhost:8084", url);
        ApiProperties properties = new ApiProperties();
        properties.setFuncionarioUrl(url);
        assertDoesNotThrow(() -> new ApiFuncionarioServiceImpl(properties));
    }

    @Test void respetaHostConfigurado() throws Exception {
        assertEquals("http://api-funcionarios-container:8084",
                resolveUrl(Map.of("FUNCIONARIO", "api-funcionarios-container")));
    }

    @Test void rechazaPlaceholderSinResolverAlIniciar() {
        ApiProperties properties = new ApiProperties();
        properties.setFuncionarioUrl("http://${FUNCIONARIO}:8084");
        var error = assertThrows(IllegalArgumentException.class,
                () -> new ApiFuncionarioServiceImpl(properties));
        assertTrue(error.getMessage().contains("FUNCIONARIO"));
    }
}
