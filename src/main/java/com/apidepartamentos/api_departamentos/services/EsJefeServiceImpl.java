package com.apidepartamentos.api_departamentos.services;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.CargoFunc;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.Departamento.NivelDepartamento;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.EsJefeService;
import com.apidepartamentos.api_departamentos.utils.RepositoryUtils;

@Service
public class EsJefeServiceImpl implements EsJefeService {

    private final DepartamentoRepository departamentoRepository;

    public EsJefeServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public CargoFunc esjefe(Long idDepto, Integer rut) {

        

        Departamento departamento = RepositoryUtils.findOrThrow(departamentoRepository.findById(idDepto),
                String.format("Departamento con id %d no encontrado", idDepto));

        boolean esJefe = departamento.getRutJefe().equals(rut);

        boolean esDirector = nivelDireccion(departamento.getNivel());

        return new CargoFunc(esJefe, esDirector);

    }

    private boolean nivelDireccion(NivelDepartamento nivel) {
        return switch (nivel) {

            case DIRECCION, ADMINISTRACION, ALCALDIA -> true;
            default -> false;
        };
    }

}
