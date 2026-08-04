package com.apidepartamentos.api_departamentos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.DepartamentoDto;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoSinJerarquiaService;

@Service
public class DepartamentoSinJerarquiaServiceImpl implements DepartamentoSinJerarquiaService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoSinJerarquiaServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public List<DepartamentoDto> listadoDepartamentos() {
        return departamentoRepository.findAll().stream()
                .map(depto -> new DepartamentoDto(depto.getId(), depto.getNombreDepartamento())).toList();
    }

}
