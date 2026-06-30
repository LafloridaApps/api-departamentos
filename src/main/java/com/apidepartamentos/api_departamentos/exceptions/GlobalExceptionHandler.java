package com.apidepartamentos.api_departamentos.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(FuncionarioResponseException.class)
    public ResponseEntity<Object> handleFuncionarioResponseException(FuncionarioResponseException ex) {
        // Log por consola para entender la excepción
        logger.error("FuncionarioResponseException capturada por el handler: {}", ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(MESSAGE_KEY, ex.getMessage(), "error", "Ocurrió un error al consultar el funcionario externo."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        logger.error("NotFoundException capturada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(MESSAGE_KEY, ex.getMessage()));
    }

    @ExceptionHandler(FuncionarioBadRequestException.class)
    public ResponseEntity<Object> handleFuncionarioBadRequestException(FuncionarioBadRequestException ex) {
        // Logueamos solo el mensaje limpio sin el stacktrace reactivo
        logger.error("FuncionarioBadRequestException capturada por el handler: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(MESSAGE_KEY, ex.getMessage(), "error", "Petición inválida al consultar API externa."));
    }
}