package com.apidepartamentos.api_departamentos.services;

import java.util.List;

import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.CodigoExternoService;

public class CodigoExternoServiceImpl implements CodigoExternoService {

    private final CodigoExternoRepository codigoExternoRepository;

    public CodigoExternoServiceImpl(CodigoExternoRepository codigoExternoRepository){
        this.codigoExternoRepository = codigoExternoRepository;
    }

    @Override
    public List<String> obtenerCodigosExternosIds(List<Long> ids) {

        List<CodigoExterno> codigoExternos = codigoExternoRepository.findAllById(ids);


        return codigoExternos.stream().map(CodigoExterno::getCodigoEx).toList();
        
    }

}
