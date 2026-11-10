package com.apidepartamentos.api_departamentos.services;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Proxy;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.apidepartamentos.api_departamentos.entities.Departamento;
import com.apidepartamentos.api_departamentos.entities.Departamento.NivelDepartamento;
import com.apidepartamentos.api_departamentos.repositories.DepartamentoRepository;

class EsJefeServiceTest {
    private Departamento actual;
    private final DepartamentoRepository repository = (DepartamentoRepository) Proxy.newProxyInstance(
            DepartamentoRepository.class.getClassLoader(), new Class<?>[]{DepartamentoRepository.class},
            (proxy, method, args) -> {
                if (method.getName().equals("findById")) return Optional.of(actual);
                throw new AssertionError("Consulta inesperada: " + method.getName());
            });
    private final EsJefeServiceImpl service = new EsJefeServiceImpl(repository);

    @Test void departamentoSinJefeNoOtorgaCargos() {
        var departamento = new Departamento();
        departamento.setNivel(NivelDepartamento.DIRECCION);
        actual = departamento;
        for (Integer rut : new Integer[]{123, null}) {
            var resultado = service.esjefe(8L, rut);
            assertFalse(resultado.isEsJefe());
            assertFalse(resultado.isEsDirector());
        }
    }

    @Test void reconoceSoloAlJefeAsignadoComoDirector() {
        var departamento = new Departamento();
        departamento.setNivel(NivelDepartamento.DIRECCION);
        departamento.setRutJefe(123);
        actual = departamento;
        var jefe = service.esjefe(8L, 123);
        assertTrue(jefe.isEsJefe());
        assertTrue(jefe.isEsDirector());
        var otro = service.esjefe(8L, 456);
        assertFalse(otro.isEsJefe());
        assertFalse(otro.isEsDirector());
    }
}
