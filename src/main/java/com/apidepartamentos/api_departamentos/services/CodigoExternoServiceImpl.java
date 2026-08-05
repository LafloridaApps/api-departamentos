package com.apidepartamentos.api_departamentos.services;

import java.util.List;
import java.util.Objects;

import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.CodigoExternoService;
import org.springframework.stereotype.Service;

@Service
public class CodigoExternoServiceImpl implements CodigoExternoService {

    private final CodigoExternoRepository codigoExternoRepository;

    public CodigoExternoServiceImpl(CodigoExternoRepository codigoExternoRepository){
        this.codigoExternoRepository = codigoExternoRepository;
    }

    @Override
    public List<String> obtenerCodigosExternosIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<Long> idsValidos = ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (idsValidos.isEmpty()) {
            return List.of();
        }

        List<CodigoExterno> codigoExternos = codigoExternoRepository.findAllByDepartamentoIdIn(idsValidos);


        return codigoExternos.stream().map(CodigoExterno::getCodigoEx).toList();
        
    }

}
