package com.example.tofi.common.persistance.domain.userservice.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Objects;

public class EmailDeserialization extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String email = p.getText();
        if (Objects.nonNull(email)) {
            return email.toLowerCase();
        } else {
            return null;
        }
    }
}