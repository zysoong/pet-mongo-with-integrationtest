package com.example.petsheltermongodemo.controller;

import com.example.petsheltermongodemo.model.Pet;
import com.example.petsheltermongodemo.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<Pet> getAllPets(){
        return this.petService.getAllPets();
    }

    @GetMapping("/{_id}")
    public Pet getPetById(@PathVariable String _id){
        return this.petService.getPetById(_id);
    }

    @PutMapping("/{_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updatePetById(@PathVariable String _id, @RequestBody Pet pet){
        this.petService.updatePetById(_id, pet);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPet(@RequestBody Pet petToAdd){
        this.petService.createPet(petToAdd);
    }

    @DeleteMapping("/{_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Pet deletePetById(@PathVariable String _id){
        Pet p = this.petService.deletePet(_id);
        return p;
    }

    @GetMapping("/search")
    public List<Pet> searchPetsWithQuery
            (
                    @RequestParam(required = false) String species,
                    @RequestParam(required = false) Integer ageGreaterThan,
                    @RequestParam(required = false) String nameContains
            )
    {
        if (species != null && ageGreaterThan == null && nameContains == null){
            return this.petService.getAllPetsWithSpecies(species);
        }

        if (species == null && ageGreaterThan != null && nameContains == null){
            return this.petService.getAllPetsElderThan(ageGreaterThan);
        }

        if (species == null && ageGreaterThan == null && nameContains != null){
            return this.petService.getAllPetsWithNameContaining(nameContains);
        }

        return new ArrayList<Pet>();
    }

}
