package com.apidepartamentos.api_departamentos.services;

import java.util.List;

import org.springframework.stereotype.Component;

import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.dto.FuncionarioResponseApi;
import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.ApiFuncionarioService;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoMapper;

@Component
public class DepartamentoMapperImpl implements DepartamentoMapper {

    private final CodigoExternoRepository codigoExternoRepository;
    private final ApiFuncionarioService apiFuncionarioService;

    public DepartamentoMapperImpl(CodigoExternoRepository codigoExternoRepository,
            ApiFuncionarioService apiFuncionarioService) {
        this.codigoExternoRepository = codigoExternoRepository;
        this.apiFuncionarioService = apiFuncionarioService;
    }

    /**
     * Public entry point. Builds a fresh cache and delegates to recursive implementation.
     */
    @Override
    public DepartamentoList maptoToDto(Departamento departamento) {
        return maptoToDto(departamento, new java.util.HashMap<>());
    }

    /**
     * Exposed overload allowing callers to provide a shared cache. Useful when
     * mapping multiple roots in one batch (see DepartamentoServiceImpl).
     */
    @Override
    public DepartamentoList maptoToDto(Departamento departamento,
            java.util.Map<Integer, FuncionarioResponseApi> jefeCache) {
        return maptoToDtoInternal(departamento, jefeCache);
    }

    /**
     * Recursive implementation that carries a map of previously fetched funcionarios.
     */
    private DepartamentoList maptoToDtoInternal(Departamento departamento,
            java.util.Map<Integer, FuncionarioResponseApi> jefeCache) {
        DepartamentoList dto = new DepartamentoList();
        dto.setId(departamento.getId());
        dto.setNombre(departamento.getNombreDepartamento());
        dto.setNivel(departamento.getNivel().toString());
        dto.setRutJefe(departamento.getRutJefe());
        dto.setCodigoExterno(getCodigoExternoByDepto(departamento));

        if (departamento.getRutJefe() != null) {
            FuncionarioResponseApi funcionario = jefeCache.computeIfAbsent(
                    departamento.getRutJefe(), this::getPersonaApi);
            dto.setNombreJefe(funcionario.getNombreCompleto());
            dto.setVrutJefe(funcionario.getVrut());
            dto.setEmail(funcionario.getEmail());
        } else {
            dto.setNombreJefe(" ");
            dto.setVrutJefe(" ");
        }

        List<Departamento> hijos = departamento.getChildrens();
        if (hijos != null && !hijos.isEmpty()) {
            dto.setDependencias(
                    hijos.stream()
                            .map(h -> maptoToDtoInternal(h, jefeCache))
                            .toList());
        }

        return dto;

    }

    private String getCodigoExternoByDepto(Departamento departamento) {
        CodigoExterno codigoExterno = codigoExternoRepository.findByDepartamento(departamento).orElse(null);
        if (codigoExterno == null) {
            return null;
        }
        return codigoExterno.getCodigoEx();
    }

    private FuncionarioResponseApi getPersonaApi(Integer rut) {

        return apiFuncionarioService.obtenerDetalleColaborador(rut);

    }

}
