package com.apidepartamentos.api_departamentos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.dto.DepartamentoResponse;
import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoMapper;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoService;
import com.apidepartamentos.api_departamentos.utils.RepositoryUtils;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

        private final CodigoExternoRepository codigoExternoRepository;

        private final DepartamentoRepository departamentoRepository;

        private final DepartamentoMapper departamentoMapper;

        public DepartamentoServiceImpl(CodigoExternoRepository codigoExternoRepository,
                        DepartamentoRepository departamentoRepository,
                DepartamentoMapper departamentoMapper) {
                this.codigoExternoRepository = codigoExternoRepository;
                this.departamentoRepository = departamentoRepository;
                this.departamentoMapper = departamentoMapper;
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
                                .map(departamentoMapper::matoToDto)
                                .toList();
        }

        
}
