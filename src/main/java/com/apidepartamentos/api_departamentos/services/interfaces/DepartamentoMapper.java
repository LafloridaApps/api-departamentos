package com.apidepartamentos.api_departamentos.services.interfaces;


import java.util.Map;

import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.dto.FuncionarioResponseApi;
import com.apidepartamentos.api_departamentos.entities.Departamento;

public interface DepartamentoMapper {

    DepartamentoList maptoToDto(Departamento departamento);

    DepartamentoList maptoToDto(Departamento departamento,
           Map<Integer, FuncionarioResponseApi> jefeCache);

}
