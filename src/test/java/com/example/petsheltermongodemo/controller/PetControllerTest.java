package com.example.petsheltermongodemo.controller;

import com.example.petsheltermongodemo.model.Pet;
import com.example.petsheltermongodemo.repository.PetRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Test
    @DirtiesContext
    void getAllPets_whenApiCalledAndListIsEmpty_thenExpectStatusOkAndReturnEmptyListAsJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    @DirtiesContext
    void getAllPets_whenApiCalledAndListIsNotEmpty_thenExpectStatusOkAndReturnListAsJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/pets")
                        .contentType("application/json")
                        .content(
                                """
                                        {
                                            "name": "Mathias",
                                            "age": 32,
                                            "species": "cat"
                                        }
                                        """
                        ))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Mathias"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(32))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].species").value("cat"));
    }

    @Test
    @DirtiesContext
    void getPetById_whenPetWithIdExists_thenReturnPetWithGivenId() throws Exception {

        ObjectMapper om = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pets")
                        .contentType("application/json")
                        .content(
                                """
                                        {
                                            "name": "Mathias",
                                            "age": 32,
                                            "species": "cat"
                                        }
                                        """
                        ))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        MvcResult resGetAll = mockMvc.perform(MockMvcRequestBuilders.get("/api/pets"))
                .andReturn();

        List<Pet> listOfPets = om.readValue(resGetAll.getResponse().getContentAsString(), new TypeReference<List<Pet>>(){});

        String id = listOfPets.get(0).petId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pets/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mathias"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(32))
                .andExpect(MockMvcResultMatchers.jsonPath("$.species").value("cat"));

    }

    @Test
    @DirtiesContext
    void searchPetsWithQuery_whenPetWithSearchingNameExists_thenReturnAllPetsContainsSearchTermOfName() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pets")
                        .contentType("application/json")
                        .content(
                                """
                                        {
                                            "name": "Mathias",
                                            "age": 32,
                                            "species": "cat"
                                        }
                                        """
                        ))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pets")
                        .contentType("application/json")
                        .content(
                                """
                                        {
                                            "name": "Matt",
                                            "age": 32,
                                            "species": "cat"
                                        }
                                        """
                        ))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pets")
                        .contentType("application/json")
                        .content(
                                """
                                        {
                                            "name": "Tadarrrr",
                                            "age": 32,
                                            "species": "cat"
                                        }
                                        """
                        ))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ObjectMapper om = new ObjectMapper();
        String matchName = "Mat";

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/api/pets/search?nameContains=" + matchName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Pet> listOfPets = om.readValue(res.getResponse().getContentAsString(), new TypeReference<List<Pet>>(){});

        for (Pet p : listOfPets){
            Assert.isTrue(p.name().contains(matchName), "Integration test failure");
        }

    }
}