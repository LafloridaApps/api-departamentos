package com.apidepartamentos.api_departamentos.services;

import com.apidepartamentos.api_departamentos.dto.DepartamentoJerarquiaDTO;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.Departamento.NivelDepartamento;
import com.apidepartamentos.api_departamentos.exceptions.NotFoundException;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.services.interfaces.JerarquiaService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class JerarquiaServiceImpl implements JerarquiaService {

    private final DepartamentoRepository departamentoRepository;

    public JerarquiaServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public DepartamentoJerarquiaDTO getJerarquiaPorId(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el departamento con id: " + id));

        if (departamento.getNivel() == NivelDepartamento.ALCALDIA) {
            List<Departamento> direcciones = departamentoRepository.findByNivel(NivelDepartamento.DIRECCION);
            List<DepartamentoJerarquiaDTO> direccionDTOs = direcciones.stream()
                    .map(dir -> new DepartamentoJerarquiaDTO(dir.getId(), dir.getNombreDepartamento(), dir.getNivel().toString(), Collections.emptyList()))
                    .toList();

            List<Departamento> administraciones = departamentoRepository.findByNivel(NivelDepartamento.ADMINISTRACION);
            List<DepartamentoJerarquiaDTO> administracionDTOs = administraciones.stream()
                    .map(admin -> new DepartamentoJerarquiaDTO(admin.getId(), admin.getNombreDepartamento(), admin.getNivel().toString(), direccionDTOs))
                    .toList();

            return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(), departamento.getNivel().toString(), administracionDTOs);

        } else if (departamento.getNivel() == NivelDepartamento.ADMINISTRACION) {
            List<Departamento> direcciones = departamentoRepository.findByNivel(NivelDepartamento.DIRECCION);
            List<DepartamentoJerarquiaDTO> hijos = direcciones.stream()
                    .map(dir -> new DepartamentoJerarquiaDTO(dir.getId(), dir.getNombreDepartamento(), dir.getNivel().toString(), Collections.emptyList()))
                    .toList();
            return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(), departamento.getNivel().toString(), hijos);
        }

        return construirJerarquia(departamento);
    }

    /** Resuelve el ámbito propio y devuelve todas sus unidades dependientes. */
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public DepartamentoJerarquiaDTO getJerarquiaDireccionPorId(Long id) {
        Departamento origen = departamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el departamento con id: " + id));
        Departamento actual = origen;
        Departamento subdireccion = null;
        Departamento administracion = null;
        Departamento alcaldia = null;
        java.util.Set<Long> visitados = new java.util.HashSet<>();
        while (actual != null) {
            validarDepartamentoNoVisitado(actual, visitados);
            if (actual.getNivel() == NivelDepartamento.DIRECCION) {
                return construirAmbitoBusqueda(actual);
            }
            if (actual.getNivel() == NivelDepartamento.SUBDIRECCION && subdireccion == null) {
                subdireccion = actual;
            }
            if (actual.getNivel() == NivelDepartamento.ADMINISTRACION && administracion == null) {
                administracion = actual;
            }
            if (actual.getNivel() == NivelDepartamento.ALCALDIA && alcaldia == null) {
                alcaldia = actual;
            }
            actual = actual.getDepartamentoSuperior();
        }
        if (subdireccion != null) {
            return construirAmbitoBusqueda(subdireccion);
        }
        if (administracion != null) {
            return construirAmbitoBusqueda(administracion);
        }
        if (alcaldia != null) {
            return construirAmbitoBusqueda(alcaldia);
        }
        throw new NotFoundException("El departamento no tiene una dirección, subdirección, administración o alcaldía superior asignada.");
    }

    private void validarDepartamentoNoVisitado(Departamento departamento, Set<Long> visitados) {
        if (departamento.getId() == null || !visitados.add(departamento.getId())) {
            throw new NotFoundException("La jerarquía del departamento contiene una relación circular.");
        }
    }

    // Administración y Alcaldía actúan como ámbitos propios para búsqueda,
    // aunque sus direcciones dependan de ellas en el organigrama.
    private DepartamentoJerarquiaDTO construirAmbitoBusqueda(Departamento departamento) {
        var hijos = departamento.getChildrens().stream()
                .filter(hijo -> hijo.getNivel() != NivelDepartamento.DIRECCION
                        && hijo.getNivel() != NivelDepartamento.ADMINISTRACION
                        && hijo.getNivel() != NivelDepartamento.ALCALDIA)
                .map(this::construirAmbitoBusqueda)
                .toList();
        return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(),
                departamento.getNivel().toString(), hijos);
    }

    private DepartamentoJerarquiaDTO construirJerarquia(Departamento departamento) {
        List<DepartamentoJerarquiaDTO> hijos = departamento.getChildrens().stream()
                .map(this::construirJerarquia)
                .toList();
        return new DepartamentoJerarquiaDTO(departamento.getId(), departamento.getNombreDepartamento(), departamento.getNivel().toString(), hijos);
    }
}
