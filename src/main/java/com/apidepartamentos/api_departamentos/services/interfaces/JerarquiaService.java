package com.apidepartamentos.api_departamentos.services.interfaces;

import com.apidepartamentos.api_departamentos.dto.DepartamentoJerarquiaDTO;

public interface JerarquiaService {
    DepartamentoJerarquiaDTO getJerarquiaPorId(Long id);
}
