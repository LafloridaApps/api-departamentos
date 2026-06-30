package com.apidepartamentos.api_departamentos.services;

import org.slf4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.LoggerFactory;

import com.apidepartamentos.api_departamentos.config.ApiProperties;
import com.apidepartamentos.api_departamentos.dto.FuncionarioResponseApi;
import com.apidepartamentos.api_departamentos.exceptions.FuncionarioBadRequestException;
import com.apidepartamentos.api_departamentos.exceptions.FuncionarioResponseException;
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
        if (rut == null) {
            logger.error("Error: El RUT proporcionado para consultar es nulo");
            throw new FuncionarioBadRequestException("El RUT del funcionario no puede ser nulo");
        }

           return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/funcionario")
                        .queryParam("rut", rut)
                        .build())
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(response -> response.value() == 404,
                          response -> {
                              logger.error("Error 404: Funcionario no encontrado con RUT: {}", rut);
                              return Mono.error(new FuncionarioResponseException("Funcionario no encontrado con RUT: " + rut));
                          })
                .onStatus(response -> response.value() == 400,
                          response -> {
                              logger.error("Error 400: Petición inválida a la API de funcionario (BAD REQUEST) con RUT: {}", rut);
                              return Mono.error(new FuncionarioBadRequestException("Petición inválida a la API externa (400 BAD REQUEST) con RUT: " + rut));
                          })
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.error("Error 4xx llamando a la API externa de funcionario: {}", response.statusCode());
                    return Mono.error(new FuncionarioResponseException("Error del cliente en API externa: " + response.statusCode()));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logger.error("Error 5xx en la API externa de funcionario: {}", response.statusCode());
                    return Mono.error(new FuncionarioResponseException("Error del servidor en API externa: " + response.statusCode()));
                })
                .bodyToMono(FuncionarioResponseApi.class)
                .onErrorResume(e -> {
                    logger.error("Error al procesar la respuesta de la API de funcionario: {}", e.getMessage());
                    if (e instanceof FuncionarioResponseException || e instanceof FuncionarioBadRequestException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new FuncionarioResponseException("Error inesperado de comunicación con API de funcionario: " + e.getMessage(), e));
                })
                .block();
    }

}
