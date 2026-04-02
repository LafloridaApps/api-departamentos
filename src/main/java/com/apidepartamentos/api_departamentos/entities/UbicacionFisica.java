package com.apidepartamentos.api_departamentos.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "ubicacion_fisica")
public class UbicacionFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreUbicacion;
    private String direccion;
    private Double latitud;
    private Double longitud;

    public UbicacionFisica() {
    }

    public UbicacionFisica(String nombreUbicacion, String direccion, Double latitud, Double longitud,
            List<Departamento> departamentos) {
        this.nombreUbicacion = nombreUbicacion;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.departamentos = departamentos;
    }

    @OneToMany(mappedBy = "ubicacionFisica")
    private List<Departamento> departamentos;

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
