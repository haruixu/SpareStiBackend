package org.ntnu.idi.idatt2106.sparesti.sparestibackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("SpareSti development API");

        Contact myContact = new Contact();
        myContact.setName("Harry Xu");
        myContact.setEmail("xulr0820@hotmail.com");

        Info information =
                new Info()
                        .title("Budgeting and saving system")
                        .version("1.0")
                        .description("This API exposes endpoints to budget and save money")
                        .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
