package com.apidepartamentos.api_departamentos.exceptioncontrollers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apidepartamentos.api_departamentos.dto.ErrorResponse;
import com.apidepartamentos.api_departamentos.exceptions.CodigExternoException;
import com.apidepartamentos.api_departamentos.exceptions.NotFoundException;
import com.apidepartamentos.api_departamentos.utils.FechaUtils;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HandlerExceptions {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerNotFoundException(NotFoundException e, HttpServletRequest request) {

        ErrorResponse error = maptoErrorResponse(e, request, HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

     @ExceptionHandler(CodigExternoException.class)
    public ResponseEntity<Object> handlerCodigoExternoException(CodigExternoException e, HttpServletRequest request) {

        ErrorResponse error = maptoErrorResponse(e, request, HttpStatus.CONFLICT);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

    }

    private <T extends Exception> ErrorResponse maptoErrorResponse(T e, HttpServletRequest request, HttpStatus status) {

        return ErrorResponse.builder()
                .timestamp(FechaUtils.getCurrentDateTime())
                .status(status.value())
                .error(status.getReasonPhrase())
                .mensaje(e.getMessage())
                .ruta(request.getRequestURI())
                .build();

    }

}
