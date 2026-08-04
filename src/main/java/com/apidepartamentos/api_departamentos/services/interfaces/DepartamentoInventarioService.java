package com.apidepartamentos.api_departamentos.services.interfaces;

import com.apidepartamentos.api_departamentos.dto.DepartamentoInventarioResponse;
import java.util.List;

public interface DepartamentoInventarioService {

    List<DepartamentoInventarioResponse> getDepartamentosList();

}
