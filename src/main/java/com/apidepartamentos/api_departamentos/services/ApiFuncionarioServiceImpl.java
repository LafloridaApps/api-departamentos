package com.apidepartamentos.api_departamentos.services;

import org.slf4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.LoggerFactory;

import com.apidepartamentos.api_departamentos.config.ApiProperties;
import com.apidepartamentos.api_departamentos.dto.FuncionarioResponseApi;
import com.apidepartamentos.api_departamentos.exceptions.NotFoundException;
import com.apidepartamentos.api_departamentos.services.interfaces.ApiFuncionarioService;

import reactor.core.publisher.Mono;

@Service
public class ApiFuncionarioServiceImpl implements ApiFuncionarioService {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiFuncionarioServiceImpl.class);

   public ApiFuncionarioServiceImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClient = webClientBuilder.baseUrl(apiProperties.getFuncionarioUrl()).build();
    }

    @Override
    public FuncionarioResponseApi obtenerDetalleColaborador(Integer rut) {
           return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/funcionario")
                        .queryParam("rut", rut)
                        .build())
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(response -> response.value() == 404, 
                          response -> Mono.error(new NotFoundException("Funcionario no encontrado con RUT: " + rut)))
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.error("Error 4xx llamando a la API externa de funcionario: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Error del cliente en API externa: " + response.statusCode() ));
                })
                .bodyToMono(FuncionarioResponseApi.class)
                .onErrorResume(e -> {
                    // Log the error but re-throw it to be handled by the global exception handler
                    logger.error("Error al procesar la respuesta de la API de funcionario", e);
                    return Mono.error(e);
                })
                .block();
    }

}
