package com.apidepartamentos.api_departamentos.services.interfaces;


import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.entities.Departamento;

public interface DepartamentoMapper {

    DepartamentoList matoToDto(Departamento departamento);

}
