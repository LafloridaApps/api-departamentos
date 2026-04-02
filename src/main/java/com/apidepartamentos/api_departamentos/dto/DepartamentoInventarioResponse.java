package com.apidepartamentos.api_departamentos.dto;

import java.util.List;

public class DepartamentoInventarioResponse {

    private Long id;
    private String nombreDepartamento;
    private List<DepartamentoInventarioResponse> dependencias;

    private DepartamentoInventarioResponse(Builder builder) {
        this.id = builder.id;
        this.nombreDepartamento = builder.nombreDepartamento;
        this.dependencias = builder.dependencias;

    }

    public Long getId() {
        return id;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public List<DepartamentoInventarioResponse> getDependencias() {
        return dependencias;
    }

    public static class Builder {

        private Long id;
        private String nombreDepartamento;
        private List<DepartamentoInventarioResponse> dependencias;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nombreDepartamento(String nombreDeparmento) {
            this.nombreDepartamento = nombreDeparmento;
            return this;
        }

        public Builder dependencias(List<DepartamentoInventarioResponse> dependencias) {
            this.dependencias = dependencias;
            return this;
        }

        public DepartamentoInventarioResponse build() {
            return new DepartamentoInventarioResponse(this);
        }

    }

}
