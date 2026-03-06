package com.apidepartamentos.api_departamentos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
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
