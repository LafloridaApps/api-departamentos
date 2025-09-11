package com.apidepartamentos.api_departamentos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apidepartamentos.api_departamentos.services.interfaces.DepartamentoService;
import com.apidepartamentos.api_departamentos.services.interfaces.EsJefeService;
import com.apidepartamentos.api_departamentos.services.interfaces.FamiliaDepartamentoService;
import com.apidepartamentos.api_departamentos.services.interfaces.JerarquiaService;
import com.apidepartamentos.api_departamentos.services.interfaces.SearchDepartamentoService;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;
    private final SearchDepartamentoService searchDepartamento;
    private final EsJefeService esJefeService;
    private final FamiliaDepartamentoService familiaDepartamentoService;
    private final JerarquiaService jerarquiaService;

    public DepartamentoController(DepartamentoService departamentoService,
            SearchDepartamentoService searchDepartamento, EsJefeService esJefeService,
            FamiliaDepartamentoService familiaDepartamentoService, JerarquiaService jerarquiaService) {
        this.departamentoService = departamentoService;
        this.searchDepartamento = searchDepartamento;
        this.esJefeService = esJefeService;
        this.familiaDepartamentoService = familiaDepartamentoService;
        this.jerarquiaService = jerarquiaService;
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

    @GetMapping("/esjefe")
    public ResponseEntity<Object> getDepartamentoJefe(
            @RequestParam Long depto, @RequestParam Integer rut) {
        return ResponseEntity.ok(esJefeService.esjefe(depto, rut));
    }

    @GetMapping("/familia/{id}")
    public ResponseEntity<Object> getFamilia(@PathVariable Long id) {
        return ResponseEntity.ok(familiaDepartamentoService.getFamilia(id));
    }

    @GetMapping("/jerarquia/{id}")
    public ResponseEntity<Object> getJerarquia(@PathVariable Long id) {
        return ResponseEntity.ok(jerarquiaService.getJerarquiaPorId(id));
    }
}
