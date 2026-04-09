package com.ead.authuser.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Microserviço de Autenticação de Usuário")
                        .version("1.0.0")
                        .description("É um microserviço do dominio de usuários, comteplando o modelo Restful"));
    }
}
