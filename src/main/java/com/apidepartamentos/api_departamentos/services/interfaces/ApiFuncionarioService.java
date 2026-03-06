package com.apidepartamentos.api_departamentos.services.interfaces;

import com.apidepartamentos.api_departamentos.dto.FuncionarioResponseApi;

public interface ApiFuncionarioService {

    FuncionarioResponseApi obtenerDetalleColaborador(Integer rut);
}
