package com.apidepartamentos.api_departamentos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import com.apidepartamentos.api_departamentos.entities.CodigoExterno;
import com.apidepartamentos.api_departamentos.entities.Departamento;

public interface CodigoExternoRepository extends JpaRepository<CodigoExterno, Long> {

    @NativeQuery("select * from codigos_externos where codigo_ex=:codigoEx")
    Optional<CodigoExterno> findByCodigoEx(String codigoEx);

    
    Optional<CodigoExterno> findByDepartamento(Departamento departamento);


}
