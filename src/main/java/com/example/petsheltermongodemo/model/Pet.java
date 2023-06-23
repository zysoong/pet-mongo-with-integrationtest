package com.example.petsheltermongodemo.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;

public record Pet(

        @MongoId
        String petId,
        String name,
        Integer age,
        String species
) {
}
