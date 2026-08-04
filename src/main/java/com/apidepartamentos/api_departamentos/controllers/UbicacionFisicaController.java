package com.apidepartamentos.api_departamentos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.apidepartamentos.api_departamentos.dto.UbicacionRequest;
import com.apidepartamentos.api_departamentos.services.interfaces.UbicacionService;

import java.util.Map;

@RestController
@RequestMapping("/api/departamentos")
public class UbicacionFisicaController {

    private final UbicacionService ubicacionService;

    public UbicacionFisicaController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @PostMapping("/ubicacion")
    public ResponseEntity<Object> agregarUbicacion(@RequestBody UbicacionRequest request) {
        ubicacionService.agregarUbicion(request);
        return ResponseEntity.ok(Map.of("message", "Ubicación agregada con éxito"));
    }

}
