package com.apidepartamentos.api_departamentos.services;

import com.apidepartamentos.api_departamentos.dto.DepartamentoJerarquiaDTO;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.Departamento.NivelDepartamento;
import com.apidepartamentos.api_departamentos.exceptions.NotFounException;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.JerarquiaService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JerarquiaServiceImpl implements JerarquiaService {

    private final DepartamentoRepository departamentoRepository;

    public JerarquiaServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public DepartamentoJerarquiaDTO getJerarquiaPorId(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NotFounException("No se encontró el departamento con id: " + id));

        if (departamento.getNivel() == NivelDepartamento.ALCALDIA) {
            List<Departamento> direcciones = departamentoRepository.findByNivel(NivelDepartamento.DIRECCION);
            List<DepartamentoJerarquiaDTO> direccionDTOs = direcciones.stream()
                    .map(dir -> new DepartamentoJerarquiaDTO(dir.getId(), dir.getNombreDepartamento(), dir.getNivel().toString(), Collections.emptyList()))
                    .collect(Collectors.toList());

            List<Departamento> administraciones = departamentoRepository.findByNivel(NivelDepartamento.ADMINISTRACION);
            List<DepartamentoJerarquiaDTO> administracionDTOs = administraciones.stream()
                    .map(admin -> new DepartamentoJerarquiaDTO(admin.getId(), admin.getNombreDepartamento(), admin.getNivel().toString(), direccionDTOs))
                    .collect(Collectors.toList());

            return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(), departamento.getNivel().toString(), administracionDTOs);

        } else if (departamento.getNivel() == NivelDepartamento.ADMINISTRACION) {
            List<Departamento> direcciones = departamentoRepository.findByNivel(NivelDepartamento.DIRECCION);
            List<DepartamentoJerarquiaDTO> hijos = direcciones.stream()
                    .map(dir -> new DepartamentoJerarquiaDTO(dir.getId(), dir.getNombreDepartamento(), dir.getNivel().toString(), Collections.emptyList()))
                    .collect(Collectors.toList());
            return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(), departamento.getNivel().toString(), hijos);
        }

        return construirJerarquia(departamento);
    }

    private DepartamentoJerarquiaDTO construirJerarquia(Departamento departamento) {
        List<DepartamentoJerarquiaDTO> hijos = departamento.getChildrens().stream()
                .map(this::construirJerarquia)
                .collect(Collectors.toList());
        return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(), departamento.getNivel().toString(), hijos);
    }
}
