package com.apidepartamentos.api_departamentos.services.interfaces;

import java.util.List;

public interface CodigoExternoService {

    List<String> obtenerCodigosExternosIds(List<Long> ids);

}
