package com.galvanize.guitars.controllers;

import com.galvanize.guitars.entities.Guitar;
import com.galvanize.guitars.services.GuitarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GuitarController.class)
public class GuitarControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GuitarService guitarService;

    private List<Guitar> guitars = new ArrayList<>();;

    @Before
    public void setUp() throws Exception {

        Guitar g = null;
        for (int i = 0; i < 10; i++) {
            g = new Guitar("Brand"+i, "Model"+i, 6);
            g.setGuitarId(Long.valueOf(i));
            guitars.add(g);
        }
    }

    @Test
    public void getAllGuitars() throws Exception {
        when(guitarService.getAll()).thenReturn(guitars);

        mvc.perform(get("/guitars/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void createGuitar() throws Exception {
        Guitar guitar = new Guitar("BRAND", "MODEL", 6);
        guitar.setGuitarId(99l);
        when(guitarService.createGuitar(any())).thenReturn(guitar);

        mvc.perform(post("/guitars").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"brand\": \"BRAND\", \"model\":\"MODEL\", \"strings\": \"6\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("guitarId").exists());
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

        when(guitarService.getGuitarById(any())).thenReturn(guitar);
        when(guitarService.updateGuitar(any())).thenReturn(guitar);

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
        when(guitarService.getGuitarById(anyLong())).thenReturn(guitar);

        guitar.setBrand(newBrand);
        guitar.setModel(newModel);
        when(guitarService.updateGuitar(any())).thenReturn(guitar);

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

        when(guitarService.deleteGuitar(anyLong())).thenReturn(true);

        //perform the delete, and make sure the return status is ok (2xx)
        mvc.perform(delete("/guitars/"+id))
                .andExpect(status().isOk());

        //make sure the guitar doesn't exist in the db
//        assertFalse(guitarRepository.findById(id).isPresent());

    }
}
