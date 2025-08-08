package com.apidepartamentos.api_departamentos.services.interfaces;

import com.apidepartamentos.api_departamentos.dto.DepartamentoResponse;
import java.util.List;

public interface FamiliaDepartamentoService {
    
    List<DepartamentoResponse> getFamilia(Long id);
}