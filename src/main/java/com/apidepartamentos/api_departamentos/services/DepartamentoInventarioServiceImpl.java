package com.apidepartamentos.api_departamentos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.DepartamentoInventarioResponse;
import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoInventarioService;

@Service
public class DepartamentoInventarioServiceImpl implements DepartamentoInventarioService {

    private final DepartamentoRepository departamentoRepository;
    private final CodigoExternoRepository codigoExternoRepository;

    public DepartamentoInventarioServiceImpl(DepartamentoRepository departamentoRepository,
            CodigoExternoRepository codigoExternoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.codigoExternoRepository = codigoExternoRepository;
    }

    @Override
    public List<DepartamentoInventarioResponse> getDepartamentosList() {

        List<Departamento> depto = departamentoRepository.findAll();

        return mapToDto(depto);

    }

    private List<DepartamentoInventarioResponse> mapToDto(List<Departamento> deptos) {

        return deptos.stream().map(depto -> new DepartamentoInventarioResponse.Builder()
                .id(depto.getId()).nombreDepartamento(depto.getNombreDepartamento())
                .dependencias(getDependencias(depto))
                .depto(getCodigoExterno(depto))
                .nivel(depto.getNivel().toString())
                .build()).toList();
    }

    private List<DepartamentoInventarioResponse> getDependencias(Departamento depto) {
        if (depto.getChildrens() != null && !depto.getChildrens().isEmpty()) {
            return mapToDto(depto.getChildrens());
        }
        return new ArrayList<>();
    }

    private String getCodigoExterno(Departamento depto) {

        Optional<CodigoExterno> codigoExterno = codigoExternoRepository.findByDepartamento(depto);
        if (codigoExterno.isPresent()) {
            return codigoExterno.get().getCodigoEx();
        }
        return null;
    }
}
