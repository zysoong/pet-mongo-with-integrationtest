package com.example.petsheltermongodemo.service;

import com.example.petsheltermongodemo.model.Pet;
import com.example.petsheltermongodemo.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<Pet> getAllPets(){
        return this.petRepository.findAll();
    }

    public Pet getPetById(String _id){
        return this.petRepository
                .findById(_id)
                .orElseThrow(() ->
                    {throw new NoSuchElementException("Pet with _id " + _id + "not found");}
                );
    }

    public void createPet(Pet petToAdd){
        this.petRepository.save(petToAdd);
    }

    public void updatePetById(String _id, Pet petToUpdate){
        this.petRepository.save(
                new Pet(_id, petToUpdate.name(), petToUpdate.age(), petToUpdate.species())
        );
    }


    public Pet deletePet(String _id){
        Pet petToDelete = this.getPetById(_id);
        this.petRepository.delete(petToDelete);
        return petToDelete;
    }

    public List<Pet> getAllPetsWithSpecies(String species){
        return this.petRepository.findAllBySpeciesIgnoreCase(species);
    }

    public List<Pet> getAllPetsElderThan(Integer age){
        return this.petRepository.findAllByAgeGreaterThanEqual(age);
    }

    public List<Pet> getAllPetsWithNameContaining(String nameContains){
        return this.petRepository.findAllByNameContainingIgnoreCase(nameContains);
    }

}
