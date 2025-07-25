package com.apidepartamentos.api_departamentos.utils;

import java.util.Optional;

import com.apidepartamentos.api_departamentos.exceptions.NotFounException;



public class RepositoryUtils {

    private RepositoryUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T findOrThrow(Optional<T> optional, String mensajeError) {
        return optional.orElseThrow(() -> new NotFounException(mensajeError));
    }

}
