package com.apidepartamentos.api_departamentos.exceptions;

public class FuncionarioResponseException extends RuntimeException {

    public FuncionarioResponseException(String mensaje) {
        super(mensaje);
    }

    public FuncionarioResponseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}