package com.apidepartamentos.api_departamentos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apidepartamentos.api_departamentos.entities.CodigoExterno;

public interface CodigoExternoRepository extends JpaRepository<CodigoExterno,Long> {


    Optional<CodigoExterno> findByCodigoEx(String codigoEx);

}
