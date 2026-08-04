package com.apidepartamentos.api_departamentos.services;

import org.springframework.stereotype.Service;

import com.apidepartamentos.api_departamentos.dto.UbicacionRequest;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.UbicacionFisica;
import com.apidepartamentos.api_departamentos.exceptions.NotFoundException;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.repositories.UbicacionFisicaRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.UbicacionService;

import jakarta.transaction.Transactional;

@Service
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionFisicaRepository ubicacionFisicaRepository;
    private final DepartamentoRepository departamentoRepository;

    public UbicacionServiceImpl(UbicacionFisicaRepository ubicacionFisicaRepository,
            DepartamentoRepository departamentoRepository) {
        this.ubicacionFisicaRepository = ubicacionFisicaRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    @Transactional
    public void agregarUbicion(UbicacionRequest request) {

        Departamento depto = obtenerDepartamento(request.idDepto());

        UbicacionFisica ubicacion = toEntity(request);

        ubicacionFisicaRepository.save(ubicacion);

        ubicacionFisicaRepository.save(ubicacion);

        depto.setUbicacionFisica(ubicacion);

        departamentoRepository.save(depto);

    }

    private UbicacionFisica toEntity(UbicacionRequest request) {
        return new UbicacionFisica(request.ubicacion(), request.direccion(), request.latitud(), request.longitud(),
                null);
    }

    private Departamento obtenerDepartamento(Long idDepto) {
        return departamentoRepository.findById(idDepto)
                .orElseThrow(() -> new NotFoundException("No se encuentra el departamento"));

    }

}
