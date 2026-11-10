package com.apidepartamentos.api_departamentos.services;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Proxy;
import java.util.*;
import org.junit.jupiter.api.Test;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.Departamento.NivelDepartamento;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;
import com.apidepartamentos.api_departamentos.exceptions.NotFoundException;

class JerarquiaDireccionTest {
    private final Map<Long, Departamento> datos = new HashMap<>();
    private final DepartamentoRepository repository = (DepartamentoRepository) Proxy.newProxyInstance(
        DepartamentoRepository.class.getClassLoader(), new Class<?>[]{DepartamentoRepository.class},
        (proxy, method, args) -> {
            if (method.getName().equals("findById")) return Optional.ofNullable(datos.get(args[0]));
            throw new AssertionError("Consulta inesperada: " + method.getName());
        });
    private final JerarquiaServiceImpl service = new JerarquiaServiceImpl(repository);

    private Departamento crear(long id, String nombre, NivelDepartamento nivel, Departamento padre) {
        var d = new Departamento(); d.setId(id); d.setNombreDepartamento(nombre); d.setNivel(nivel);
        d.setDepartamentoSuperior(padre);
        if (padre != null) padre.getChildrens().add(d);
        datos.put(id, d);
        return d;
    }
    @Test void desarrolloYAnalisisIncluyeTodaTecnologia() {
        var direccion = crear(8, "Tecnologías de la Información", NivelDepartamento.DIRECCION, null);
        crear(88, "Desarrollo y Análisis de Datos", NivelDepartamento.DEPARTAMENTO, direccion);
        var hermano = crear(89, "Otro departamento", NivelDepartamento.DEPARTAMENTO, direccion);
        crear(190, "Sección", NivelDepartamento.SECCION, hermano);
        var resultado = service.getJerarquiaDireccionPorId(88L);
        assertEquals(8L, resultado.getId());
        assertEquals(List.of(88L, 89L), resultado.getHijos().stream().map(h -> h.getId()).toList());
        assertEquals(190L, resultado.getHijos().get(1).getHijos().getFirst().getId());
    }
    @Test void subdireccionIntermediaNoImpideSubirALaDireccion() {
        var direccion = crear(8, "Dirección", NivelDepartamento.DIRECCION, null);
        var sub = crear(80, "Subdirección", NivelDepartamento.SUBDIRECCION, direccion);
        crear(88, "Departamento", NivelDepartamento.DEPARTAMENTO, sub);
        crear(89, "Departamento hermano", NivelDepartamento.DEPARTAMENTO, direccion);
        assertEquals(8L, service.getJerarquiaDireccionPorId(88L).getId());
        assertEquals(8L, service.getJerarquiaDireccionPorId(80L).getId());
        assertEquals(8L, service.getJerarquiaDireccionPorId(8L).getId());
    }
    @Test void usaSubdireccionSiNoExisteDireccionSuperior() {
        var admin = crear(2, "Administración", NivelDepartamento.ADMINISTRACION, null);
        var sub = crear(80, "Subdirección", NivelDepartamento.SUBDIRECCION, admin);
        crear(88, "Departamento", NivelDepartamento.DEPARTAMENTO, sub);
        crear(99, "Otra dirección", NivelDepartamento.DIRECCION, admin);
        var resultado = service.getJerarquiaDireccionPorId(88L);
        assertEquals(80L, resultado.getId());
        assertEquals(1, resultado.getHijos().size());
    }
    @Test void administracionIncluyeUnidadesPropiasYExcluyeDirecciones() {
        var alcaldia = crear(1, "Alcaldía", NivelDepartamento.ALCALDIA, null);
        var admin = crear(2, "Administración", NivelDepartamento.ADMINISTRACION, alcaldia);
        crear(189, "Estadio", NivelDepartamento.DEPARTAMENTO, admin);
        crear(99, "Dirección dependiente", NivelDepartamento.DIRECCION, admin);
        crear(100, "Otra unidad", NivelDepartamento.DEPARTAMENTO, alcaldia);
        var resultado = service.getJerarquiaDireccionPorId(189L);
        assertEquals(2L, resultado.getId());
        assertEquals("ADMINISTRACION", resultado.getNivel());
        assertEquals(List.of(189L), resultado.getHijos().stream().map(h -> h.getId()).toList());
        assertEquals(2L, service.getJerarquiaDireccionPorId(2L).getId());
    }
    @Test void resuelveCadaAmbitoDentroDeLaJerarquiaMunicipalCompleta() {
        var alcaldia = crear(1, "Alcaldía", NivelDepartamento.ALCALDIA, null);
        var admin = crear(2, "Administración", NivelDepartamento.ADMINISTRACION, alcaldia);
        var direccion = crear(8, "Dirección", NivelDepartamento.DIRECCION, admin);
        crear(88, "Departamento", NivelDepartamento.DEPARTAMENTO, direccion);
        crear(89, "Departamento hermano", NivelDepartamento.DEPARTAMENTO, direccion);
        crear(9, "Otra dirección", NivelDepartamento.DIRECCION, admin);
        crear(189, "Estadio", NivelDepartamento.DEPARTAMENTO, admin);
        crear(100, "Unidad directa de Alcaldía", NivelDepartamento.DEPARTAMENTO, alcaldia);
        assertEquals(8L, service.getJerarquiaDireccionPorId(88L).getId());
        assertEquals(8L, service.getJerarquiaDireccionPorId(8L).getId());
        assertEquals(2L, service.getJerarquiaDireccionPorId(189L).getId());
        assertEquals(2L, service.getJerarquiaDireccionPorId(2L).getId());
        assertEquals(1L, service.getJerarquiaDireccionPorId(100L).getId());
        var resultado = service.getJerarquiaDireccionPorId(1L);
        assertEquals("ALCALDIA", resultado.getNivel());
        assertEquals(List.of(100L), resultado.getHijos().stream().map(h -> h.getId()).toList());
        var ambitoAdmin = service.getJerarquiaDireccionPorId(189L);
        assertEquals(List.of(189L), ambitoAdmin.getHijos().stream().map(h -> h.getId()).toList());
        var ambitoDireccion = service.getJerarquiaDireccionPorId(88L);
        assertEquals(List.of(88L, 89L), ambitoDireccion.getHijos().stream().map(h -> h.getId()).toList());
    }
    @Test void cortaDireccionesInclusoDentroDeUnDepartamentoSinAlterarElOrganigrama() {
        var admin = crear(2, "Administración", NivelDepartamento.ADMINISTRACION, null);
        var departamento = crear(189, "Estadio", NivelDepartamento.DEPARTAMENTO, admin);
        crear(190, "Sección propia", NivelDepartamento.SECCION, departamento);
        var direccion = crear(8, "Dirección", NivelDepartamento.DIRECCION, departamento);
        crear(88, "Departamento de dirección", NivelDepartamento.DEPARTAMENTO, direccion);
        var resultado = service.getJerarquiaDireccionPorId(189L);
        assertEquals(List.of(190L), resultado.getHijos().getFirst().getHijos().stream().map(h -> h.getId()).toList());
        assertEquals(2, service.getJerarquiaPorId(189L).getHijos().size());
        assertEquals(8L, service.getJerarquiaDireccionPorId(88L).getId());
    }
    @Test void detectaCiclos() {
        var uno = crear(88, "Departamento", NivelDepartamento.DEPARTAMENTO, null);
        var dos = crear(89, "Departamento", NivelDepartamento.DEPARTAMENTO, uno);
        uno.setDepartamentoSuperior(dos);
        assertThrows(NotFoundException.class, () -> service.getJerarquiaDireccionPorId(88L));
    }
    @Test void informaDepartamentoInexistente() {
        assertThrows(NotFoundException.class, () -> service.getJerarquiaDireccionPorId(999L));
    }
}
