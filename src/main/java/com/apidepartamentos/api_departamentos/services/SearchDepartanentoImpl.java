package com.apidepartamentos.api_departamentos.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.DepartamentoResponse;
import com.apidepartamentos.api_departamentos.dto.SearchDepartamentoResponse;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.SearchDepartamentoService;

@Service
public class SearchDepartanentoImpl implements SearchDepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public SearchDepartanentoImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public SearchDepartamentoResponse findByNombreDepto(String nombreDepto, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("nombreDepartamento").ascending());

        Page<Departamento> pageDepartamentos = departamentoRepository
                .findByNombreDepartamentoContainingIgnoreCase(nombreDepto, pageable);

        SearchDepartamentoResponse response = new SearchDepartamentoResponse();

        response.setCurrentPage(pageDepartamentos.getNumber());
        response.setTotalItems(pageDepartamentos.getNumberOfElements());
        response.setTotalPages(pageDepartamentos.getTotalPages());
        response.setSize(pageDepartamentos.getTotalElements());

        List<DepartamentoResponse> departamentos = pageDepartamentos.getContent().stream()
                .map(departamento -> new DepartamentoResponse(departamento.getId(),
                        departamento.getNombreDepartamento(), departamento.getRutJefe()))
                .toList();

        response.setDepartamentos(departamentos);

        return response;

    }

}
