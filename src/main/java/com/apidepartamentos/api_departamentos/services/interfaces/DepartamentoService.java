package com.apidepartamentos.api_departamentos.services.interfaces;

import java.util.List;

import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.dto.DepartamentoResponse;

public interface DepartamentoService {

    DepartamentoResponse getDeparamentoByCodigoExt(String codEx);


    DepartamentoResponse getDepartamentoById(Long id);

    List<DepartamentoList> getDepartamentosList();



}
