package com.example.demo.Swager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(

        info = @Info(

                contact = @Contact(
                        name="AhmedYassin",
                        email="ahmedyassinzouaoui@gmail.com"
                ),

                description="openAPI documentaion ",
                title = "openApi specification - trao"
        ),


          security ={

        @SecurityRequirement(
                name = "bearerAuth"
        )

}
)

@SecurityScheme(

        name = "bearerAuth",
        description = "JWT Auth description",
        scheme = "bearer",
        type= SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER


)
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${app.server.url}") String serverUrl) {

        return new OpenAPI()
                .servers(List.of(
                        new io.swagger.v3.oas.models.servers.Server()
                                .url(serverUrl)
                                .description("Current Environment")

                ));
    }


}