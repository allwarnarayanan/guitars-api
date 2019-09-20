package com.galvanize.guitars.controllers;

import com.galvanize.guitars.entities.Guitar;
import com.galvanize.guitars.repositories.GuitarRepository;
import com.galvanize.guitars.services.GuitarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class GuitarControllerTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    GuitarRepository guitarRepository;

    @Autowired
    GuitarService guitarService;


    private List<Guitar> guitars = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        for (int i = 0; i < 10; i++) {
            guitars.add(new Guitar("Brand"+i, "Model"+i, 6));
        }
        guitarRepository.saveAll(guitars);
    }

    @Test
    public void getAllGuitars() throws Exception {
        mvc.perform(get("/guitars/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void createGuitar() throws Exception {
        mvc.perform(post("/guitars").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"brand\": \"BRAND\", \"model\":\"MODEL\", \"strings\": \"6\" }"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("guitarId").exists());
    }

    @Test
    public void updateGuitarWithPut() throws Exception {
        //Get a guitar from the ones we created in setup
        Guitar guitar = guitars.get(3);
        //Change something on that guitar
        guitar.setModel("Model that's Something totally different");
        //Manually convert it to json (there are other ways, but I wasn't sure if you knew them yet)
        String json = String.format("{ \"guitarId\": \"%s\", \"brand\": \"%s\", \"model\":\"%s\", \"strings\": \"%s\" }",
                guitar.getGuitarId(), guitar.getBrand(), guitar.getModel(), guitar.getStrings());

        mvc.perform(put("/guitars/"+guitar.getGuitarId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("guitarId").value(guitar.getGuitarId()))
                .andExpect(jsonPath("model").value("Model that's Something totally different"));
    }

    @Test
    public void updateGuitarWithPatch() throws Exception {
        Guitar guitar = guitars.get(5);
        String newBrand = "Updated Brand";
        String newModel = "Updated Model";
        String jsonData = String.format("{ \"model\": \"%s\", \"brand\": \"%s\" }",
                                        newModel, newBrand);
        mvc.perform(patch("/guitars/"+guitar.getGuitarId()).contentType(MediaType.APPLICATION_JSON).content(jsonData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("model").value(newModel))
                .andExpect(jsonPath("brand").value(newBrand))
                .andExpect(jsonPath("strings").value(guitar.getStrings()));
    }

    @Test
    public void deleteGuitar() throws Exception {
        //Pick a guitar from the ones we create in setup
        long id = guitars.get(8).getGuitarId();

        //perform the delete, and make sure the return status is ok (2xx)
        mvc.perform(delete("/guitars/"+id))
                .andExpect(status().isOk());

        //make sure the guitar doesn't exist in the db
        assertFalse(guitarRepository.findById(id).isPresent());

    }
}
