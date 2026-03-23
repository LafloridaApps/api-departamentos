package com.apidepartamentos.api_departamentos.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.DepartamentoList;
import com.apidepartamentos.api_departamentos.dto.DepartamentoResponse;
import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.Departamento.NivelDepartamento;
import com.apidepartamentos.api_departamentos.exceptions.CodigExternoException;
import com.apidepartamentos.api_departamentos.repositories.CodigoExternoRepository;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoMapper;
import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoService;
import com.apidepartamentos.api_departamentos.dto.FuncionarioResponseApi;
import com.apidepartamentos.api_departamentos.utils.RepositoryUtils;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

        private final CodigoExternoRepository codigoExternoRepository;

        private final DepartamentoRepository departamentoRepository;

        private final DepartamentoMapper departamentoMapper;

        private static final String MESSAGE = "No Existe el deparamento ";

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

                Departamento departamento = getDepartamentoRepositoryById(codigoExterno.getIdDepto());

                return new DepartamentoResponse(departamento.getId(), departamento.getNombreDepartamento(),
                                departamento.getRutJefe(), departamento.getRutJefeSuperior(),
                                departamento.getIdDepartamentoSuperior(), departamento.getNivel().name());

        }

        @Override
        public DepartamentoResponse getDepartamentoById(Long id) {
                Departamento departamento = getDepartamentoRepositoryById(id);

                return new DepartamentoResponse(departamento.getId(), departamento.getNombreDepartamento(),
                                departamento.getRutJefe(), departamento.getRutJefeSuperior(),
                                departamento.getIdDepartamentoSuperior(), departamento.getNivel().name());
        }

        @Override
        public List<DepartamentoList> getDepartamentosList() {
                List<Departamento> raices = departamentoRepository.findByNivel(Departamento.NivelDepartamento.ALCALDIA);

                java.util.Map<Integer, FuncionarioResponseApi> jefeCache = new HashMap<>();

                return raices.stream()
                                .map(d -> departamentoMapper.maptoToDto(d, jefeCache))
                                .toList();
        }

        @Override
        public void updateJefeDepartamento(Long idDepartamento, Integer rut) {
                Departamento departamento = getDepartamentoRepositoryById(idDepartamento);
                if (rut == 0) {
                        rut = null;
                }

                departamento.setRutJefe(rut);

                departamentoRepository.save(departamento);

        }

        @Override
        public void updteNombreDeparamento(Long idDepartamento, String nombre) {
                Departamento departamento = getDepartamentoRepositoryById(idDepartamento);

                if (!departamento.getNombreDepartamento().equalsIgnoreCase(nombre)) {

                        departamento.setNombreDepartamento(nombre);
                        departamentoRepository.save(departamento);

                }

        }

        @Override
        public void updateCodigoExterno(Long idDepartamento, String codigoExterno) {

                Optional<CodigoExterno> optCodigoExterno = codigoExternoRepository.findByCodigoEx(codigoExterno);

                if (optCodigoExterno.isPresent()) {
                        throw new CodigExternoException("Ya existe un departamento con este código");
                }

                Departamento depto = getDepartamentoRepositoryById(idDepartamento);

                CodigoExterno codEx = new CodigoExterno();
                codEx.setDepartamento(depto);
                codEx.setCodigoEx(codigoExterno);

                codigoExternoRepository.save(codEx);

        }

        @Override
        public void deleteById(Long idDepartamento) {

                CodigoExterno codigoExterno = RepositoryUtils.findOrThrow(
                                codigoExternoRepository
                                                .findByDepartamento(getDepartamentoRepositoryById(idDepartamento)),
                                String.format("El departametno con el codigo %s", idDepartamento));

                codigoExternoRepository.deleteById(codigoExterno.getId());

        }

        private Departamento getDepartamentoRepositoryById(Long id) {
                return RepositoryUtils.findOrThrow(departamentoRepository.findById(id),
                                String.format(MESSAGE + "%d", id));
        }

        @Override
        public void agregarDepartamento(Long idPadre, String nombreDepartameto, Integer rut) {

                Departamento padre = getDepartamentoRepositoryById(idPadre);

                Departamento depto = new Departamento();
                depto.setNombreDepartamento(nombreDepartameto);
                if (rut != null) {
                        depto.setRutJefe(rut);
                }

                depto.setDepartamentoSuperior(padre);
                depto.setNivel(getNiDepartamento(padre));

                departamentoRepository.save(depto);

        }

        private NivelDepartamento getNiDepartamento(Departamento padre) {

                return switch (padre.getNivel()) {
                        case ALCALDIA -> NivelDepartamento.DIRECCION;
                        case DIRECCION -> NivelDepartamento.DEPARTAMENTO;
                        case DEPARTAMENTO -> NivelDepartamento.SECCION;
                        case SECCION -> NivelDepartamento.OFICINA;
                        default -> NivelDepartamento.ALCALDIA;
                };

        }

}