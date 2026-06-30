package com.apidepartamentos.api_departamentos.exceptions;

public class FuncionarioBadRequestException extends RuntimeException {

    public FuncionarioBadRequestException(String mensaje) {
        super(mensaje);
    }

    public FuncionarioBadRequestException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}