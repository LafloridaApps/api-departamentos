package com.apidepartamentos.api_departamentos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoService;
import com.apidepartamentos.api_departamentos.services.interfaces.SearchDepartamentoService;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    private final SearchDepartamentoService searchDepartamento;

    public DepartamentoController(DepartamentoService departamentoService,
            SearchDepartamentoService searchDepartamento) {
        this.departamentoService = departamentoService;
        this.searchDepartamento = searchDepartamento;
    }

    @GetMapping("/codex")
    public ResponseEntity<Object> getDeptoByCodEx(@RequestParam String codex) {
        return ResponseEntity.ok(departamentoService.getDeparamentoByCodigoExt(codex));
    }

    @GetMapping
    public ResponseEntity<Object> getDeptoById(@RequestParam Long id) {
        return ResponseEntity.ok(departamentoService.getDepartamentoById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getDeptoList() {
        return ResponseEntity.ok(departamentoService.getDepartamentosList());
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getDepartamentoByNombre(
            @RequestParam String pattern, @RequestParam int pageNumber) {
        return ResponseEntity.ok(searchDepartamento.findByNombreDepto(pattern, pageNumber));
    }

}
