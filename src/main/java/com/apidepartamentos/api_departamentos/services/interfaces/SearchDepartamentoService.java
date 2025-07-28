package com.apidepartamentos.api_departamentos.services.interfaces;

import com.apidepartamentos.api_departamentos.dto.SearchDepartamentoResponse;

public interface SearchDepartamentoService {

    SearchDepartamentoResponse findByNombreDepto(String nombreDepto, int pageNumber);

}
