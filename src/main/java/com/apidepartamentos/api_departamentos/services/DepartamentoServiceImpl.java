package com.apidepartamentos.api_departamentos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.dto.DepartamentoResponse;
import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoService;
import com.apidepartamentos.api_departamentos.utils.RepositoryUtils;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

        private final CodigoExternoRepository codigoExternoRepository;

        private final DepartamentoRepository departamentoRepository;

        public DepartamentoServiceImpl(CodigoExternoRepository codigoExternoRepository,
                        DepartamentoRepository departamentoRepository) {
                this.codigoExternoRepository = codigoExternoRepository;
                this.departamentoRepository = departamentoRepository;
        }

        @Override
        public DepartamentoResponse getDeparamentoByCodigoExt(String codEx) {

                CodigoExterno codigoExterno = RepositoryUtils.findOrThrow(codigoExternoRepository.findByCodigoEx(codEx),
                                String.format("El departametno con el codigo %s", codEx));

                Departamento departamento = RepositoryUtils.findOrThrow(
                                departamentoRepository.findById(codigoExterno.getIdDepto()),
                                String.format("El departamento con el codigo %s", codEx));

                return new DepartamentoResponse(departamento.getId(), departamento.getNombreDepartamento(),
                                departamento.getRutJefe(), departamento.getRutJefeSuperior(),
                                departamento.getIdDepartamentoSuperior(),departamento.getNivel().name() );

        }

        @Override
        public DepartamentoResponse getDepartamentoById(Long id) {
                Departamento departamento = RepositoryUtils.findOrThrow(
                                departamentoRepository.findById(id),
                                String.format("El departamento con el codigo %d", id));

                return new DepartamentoResponse(departamento.getId(), departamento.getNombreDepartamento(),
                                departamento.getRutJefe(), departamento.getRutJefeSuperior(),
                                departamento.getIdDepartamentoSuperior(),departamento.getNivel().name());
        }

        @Override
        public List<DepartamentoList> getDepartamentosList() {
                List<Departamento> raices = departamentoRepository.findAll()
                                .stream()
                                .filter(d -> d.getNivel().toString().equalsIgnoreCase("ALCALDIA"))
                                .toList();

                return raices.stream()
                                .map(this::mapDto)
                                .toList();
        }

        private DepartamentoList mapDto(Departamento departamento) {
                DepartamentoList dto = new DepartamentoList();
                dto.setId(departamento.getId());
                dto.setNombre(departamento.getNombreDepartamento());
                dto.setNivel(departamento.getNivel().toString());
                dto.setRutJefe(departamento.getRutJefe());

                List<Departamento> hijos = departamento.getChildrens();
                if (hijos != null && !hijos.isEmpty()) {
                        dto.setDependencias(
                                        hijos.stream()
                                                        .map(this::mapDto)
                                                        .toList());
                }

                return dto;
        }

}
