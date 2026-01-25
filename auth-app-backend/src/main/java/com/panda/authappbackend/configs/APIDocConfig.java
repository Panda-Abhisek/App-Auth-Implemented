package com.panda.authappbackend.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Application build by Abhisek Panda.",
                description = "Generic auth app that can be used with any application.",
                contact = @Contact(
                        name = "Durgesh Kumar Tiwari",
                        url = "https://www.linkedin.com/in/abhisek-panda-",
                        email = "abhisekpanda114@gmail.com"
                ),
                version = "1.0",
                summary = "This app is very useful if you dont want create auth app from scratch."
        )
        ,
        security = {
                @SecurityRequirement(
                        name="bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer", //Authorization: Bearer htokenaswga,
        bearerFormat = "JWT"
)
public class APIDocConfig {}