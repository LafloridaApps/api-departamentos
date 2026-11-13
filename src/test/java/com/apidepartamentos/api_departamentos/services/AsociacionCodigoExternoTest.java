package com.apidepartamentos.api_departamentos.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.apidepartamentos.api_departamentos.entities.*;
import com.apidepartamentos.api_departamentos.repositories.*;
import com.apidepartamentos.api_departamentos.exceptions.CodigExternoException;

class AsociacionCodigoExternoTest {
    @Test
    void noAgregaSegundoCodigoAUnDepartamentoYaAsociado() {
        var codigos = mock(CodigoExternoRepository.class);
        var departamentos = mock(DepartamentoRepository.class);
        var departamento = new Departamento();
        when(departamentos.findById(190L)).thenReturn(Optional.of(departamento));
        when(codigos.findByCodigoEx("17100801")).thenReturn(Optional.empty());
        when(codigos.findByDepartamento(departamento)).thenReturn(Optional.of(new CodigoExterno()));
        var service = new DepartamentoServiceImpl(codigos, departamentos, null);
        assertThrows(CodigExternoException.class, () -> service.updateCodigoExterno(190L, "17100801"));
        verify(codigos, never()).save(any());
    }

    @Test
    void asociaCodigoAlDepartamentoSeleccionado() {
        var codigos = mock(CodigoExternoRepository.class);
        var departamentos = mock(DepartamentoRepository.class);
        var departamento = new Departamento();
        when(departamentos.findById(190L)).thenReturn(Optional.of(departamento));
        when(codigos.findByCodigoEx("17100801")).thenReturn(Optional.empty());
        when(codigos.findByDepartamento(departamento)).thenReturn(Optional.empty());
        new DepartamentoServiceImpl(codigos, departamentos, null).updateCodigoExterno(190L, "17100801");
        verify(codigos).save(argThat(c -> "17100801".equals(c.getCodigoEx()) && c.getDepartamento() == departamento));
    }
}
