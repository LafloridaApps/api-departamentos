package com.apidepartamentos.api_departamentos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apidepartamentos.api_departamentos.dto.DepartamentoRequest;
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
    private static final String MESSAGE_VALUE = "message";

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

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateJedeDEpartamento(@PathVariable Long id, @RequestParam Integer rut) {

        departamentoService.updateJefeDepartamento(id, rut);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(MESSAGE_VALUE, "Departametno acutalizado con éxito"));

    }

    @PutMapping("/{id}/nombre")
    public ResponseEntity<Object> updateNombreDeparatmento(@PathVariable Long id, @RequestParam String nombre) {

        if (nombre == null || nombre.isBlank() || nombre.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(MESSAGE_VALUE, "El nombre no puede ser nulo o blanco"));
        }

        departamentoService.updteNombreDeparamento(id, nombre);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(MESSAGE_VALUE, "Departametno actualizado con éxito"));

    }

    @PutMapping("/{id}/codigoEx")
    public ResponseEntity<Object> updateCodigoExterno(@PathVariable Long id, @RequestParam String codigoEx) {

        if (codigoEx == null || codigoEx.isBlank() || codigoEx.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(MESSAGE_VALUE, "El codigo no puede ser nulo o blanco"));
        }

        departamentoService.updateCodigoExterno(id, codigoEx);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(MESSAGE_VALUE, "Código acutalizado con éxito"));

    }

    @DeleteMapping("/{idDepto}/codex-del")
    public ResponseEntity<Object> deleteCodigoExtgernoByIdDepto(@PathVariable Long idDepto) {
        departamentoService.deleteById(idDepto);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(MESSAGE_VALUE, "Código eliminado con éxito"));
    }

    @PostMapping("/add")
    public ResponseEntity<Object> agregaDepartamento(@RequestBody DepartamentoRequest request) {
        departamentoService.agregarDepartamento(request.idPadre(), request.nombreDepartamento(), request.rutJefe());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(MESSAGE_VALUE, "Departamento agregado con éxito"));

    }

 

}
