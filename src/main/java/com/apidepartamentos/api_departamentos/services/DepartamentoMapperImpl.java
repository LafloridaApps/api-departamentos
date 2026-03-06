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

    @Override
    public DepartamentoList matoToDto(Departamento departamento) {
        DepartamentoList dto = new DepartamentoList();
        dto.setId(departamento.getId());
        dto.setNombre(departamento.getNombreDepartamento());
        dto.setNivel(departamento.getNivel().toString());
        dto.setRutJefe(departamento.getRutJefe());
        dto.setCodigoExterno(getCodigoExternoByDepto(departamento));

        FuncionarioResponseApi  funcionario = new FuncionarioResponseApi();
        
        if(departamento.getRutJefe() != null){
             funcionario = getPersonaApi(departamento.getRutJefe());
             dto.setNombreJefe(funcionario.getNombreCompleto());
             dto.setVrutJefe(funcionario.getVrut());
        }else{
            dto.setNombreJefe(" ");
            dto.setVrutJefe(" ");
            
        }
        

        dto.setNombreJefe(funcionario.getNombreCompleto());
        dto.setVrutJefe(funcionario.getVrut());

        List<Departamento> hijos = departamento.getChildrens();
        if (hijos != null && !hijos.isEmpty()) {
            dto.setDependencias(
                    hijos.stream()
                            .map(this::matoToDto)
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
