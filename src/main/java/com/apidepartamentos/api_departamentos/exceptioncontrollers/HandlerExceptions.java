package com.apidepartamentos.api_departamentos.exceptioncontrollers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apidepartamentos.api_departamentos.dto.ErrorResponse;
import com.apidepartamentos.api_departamentos.exceptions.NotFounException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HandlerExceptions {

      @ExceptionHandler(NotFounException.class)
    public ResponseEntity<Object> handlerNotFoundException(NotFounException e, HttpServletRequest request) {

        ErrorResponse error = maptoErrorResponse(e, request, HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }


    private <T extends Exception> ErrorResponse maptoErrorResponse(T e, HttpServletRequest request, HttpStatus status) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .mensaje(e.getMessage())
                .ruta(request.getRequestURI())
                .build();

    }

}
