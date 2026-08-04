package com.apidepartamentos.api_departamentos.dto;

public record UbicacionRequest(
    String direccion,
    Double latitud,
    Double longitud,
    String ubicacion,
    Long idDepto
) {

}
