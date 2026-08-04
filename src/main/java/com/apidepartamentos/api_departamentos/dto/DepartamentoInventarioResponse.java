package com.apidepartamentos.api_departamentos.dto;

import java.util.List;

public class DepartamentoInventarioResponse {

    private Long id;
    private String nombreDepartamento;
    private List<DepartamentoInventarioResponse> dependencias;
    private String nivel;
    private String depto;

    private DepartamentoInventarioResponse(Builder builder) {
        this.id = builder.id;
        this.nombreDepartamento = builder.nombreDepartamento;
        this.dependencias = builder.dependencias;
        this.nivel = builder.nivel;
        this.depto = builder.depto;

    }

    public String getNivel() {
        return nivel;
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

    public String getDepto() {
        return depto;
    }

    public static class Builder {

        private Long id;
        private String nombreDepartamento;
        private List<DepartamentoInventarioResponse> dependencias;
        private String nivel;
        private String depto;

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

        public Builder nivel(String nivel) {
            this.nivel = nivel;
            return this;
        }

        public Builder depto(String depto) {
            this.depto = depto;
            return this;
        }

        public DepartamentoInventarioResponse build() {
            return new DepartamentoInventarioResponse(this);
        }

    }

}
