package com.apidepartamentos.api_departamentos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private String funcionarioUrl;

    public String getFuncionarioUrl() {
        return funcionarioUrl;
    }

    public void setFuncionarioUrl(String funcionarioUrl) {
        this.funcionarioUrl = funcionarioUrl;
    }

   

    

}
