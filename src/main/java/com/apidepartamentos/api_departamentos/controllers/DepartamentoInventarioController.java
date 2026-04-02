package com.apidepartamentos.api_departamentos.controllers;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoInventarioService;



@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoInventarioController {

    private final DepartamentoInventarioService departamentoInventarioService;

    public DepartamentoInventarioController(DepartamentoInventarioService departamentoInventarioService) {
        this.departamentoInventarioService = departamentoInventarioService;
    }

    @GetMapping("/inventario")
    public ResponseEntity<Object> getInventario() {
        return ResponseEntity.ok(departamentoInventarioService.getDepartamentosList());
    }


}
