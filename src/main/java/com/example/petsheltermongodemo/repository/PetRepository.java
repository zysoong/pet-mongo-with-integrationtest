package com.example.petsheltermongodemo.repository;

import com.example.petsheltermongodemo.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends MongoRepository<Pet, String> {

    List<Pet> findAllBySpeciesIgnoreCase(String species);
    List<Pet> findAllByAgeGreaterThanEqual(Integer ageGreaterThan);
    List<Pet> findAllByNameContainingIgnoreCase(String nameContains);

}
