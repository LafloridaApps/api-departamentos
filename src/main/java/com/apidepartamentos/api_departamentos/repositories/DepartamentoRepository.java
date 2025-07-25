package com.apidepartamentos.api_departamentos.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.apidepartamentos.api_departamentos.entities.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento,Long> {

    

}
