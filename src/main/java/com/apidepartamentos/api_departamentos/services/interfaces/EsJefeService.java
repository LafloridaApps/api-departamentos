package com.apidepartamentos.api_departamentos.services.interfaces;

import com.apidepartamentos.api_departamentos.dto.CargoFunc;

public interface EsJefeService {

    CargoFunc esjefe(Long idDepto, Integer rut);

}
