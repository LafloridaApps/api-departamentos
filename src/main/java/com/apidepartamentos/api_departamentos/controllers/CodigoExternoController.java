package com.apidepartamentos.api_departamentos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apidepartamentos.api_departamentos.services.interfaces.CodigoExternoService;

@RestController
@RequestMapping("/api/departamentos")
public class CodigoExternoController {

    private final CodigoExternoService codigoExternoService;

    public CodigoExternoController(CodigoExternoService codigoExternoService) {
        this.codigoExternoService = codigoExternoService;
    }

    @PostMapping("/codigos-externos")
    public ResponseEntity<Object> obtenerCodigosExternosIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(codigoExternoService.obtenerCodigosExternosIds(ids));
    }

}
