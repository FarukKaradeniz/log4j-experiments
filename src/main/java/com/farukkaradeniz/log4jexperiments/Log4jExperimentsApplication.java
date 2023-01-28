package com.farukkaradeniz.log4jexperiments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@EnableFeignClients
@SpringBootApplication
public class Log4jExperimentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Log4jExperimentsApplication.class, args);
    }


    @Bean
    ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(getJavaTimeModule());
        return mapper;
    }

    private JavaTimeModule getJavaTimeModule() {
        var module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(offsetDateTime.format(formatter()));
            }
        });
        module.addDeserializer(OffsetDateTime.class, new JsonDeserializer<>() {
            @Override
            public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return OffsetDateTime.parse(jsonParser.getText(), formatter());
            }
        });
        return module;
    }


    private DateTimeFormatter formatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

}
