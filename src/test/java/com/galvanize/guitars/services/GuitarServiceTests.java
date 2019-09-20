package com.galvanize.guitars.services;

import com.galvanize.guitars.entities.Guitar;
import com.galvanize.guitars.repositories.GuitarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class GuitarServiceTests {

    @Autowired
    GuitarRepository guitarRepository;

    @Autowired
    GuitarService guitarService;

    @Test
    public void getAllGuitars() {
        List<Guitar> guitars = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            guitars.add(new Guitar("Brand"+i, "Model"+i, 6));
        }
        guitarRepository.saveAll(guitars);

        List<Guitar> svcGuitars = guitarService.getAll();

        for(Guitar guitar : svcGuitars) {
            assertNotNull(guitar.getGuitarId());
        }

    }

    @Test
    public void getOneGuitar() {
        Guitar guitar = new Guitar("Guild", "D45Bld", 6);
        guitarRepository.save(guitar);

        Guitar newGuitar = guitarService.getGuitarById(guitar.getGuitarId());

        assertNotNull(newGuitar.getGuitarId());
    }

    @Test
    public void createGuitar() {
        Guitar guitar = new Guitar("Guild", "D45Bld", 6);

        guitar = guitarService.createGuitar(guitar);

        assertNotNull(guitar.getGuitarId());
    }

    @Test
    public void deleteGuitar() {
        Guitar guitar = new Guitar("Guild", "D45Bld", 6);
        guitarRepository.save(guitar);
        Long guitarId = guitar.getGuitarId();

        boolean deleted = guitarService.deleteGuitar(guitarId);

        assertTrue(deleted);

        Optional<Guitar> deletedGuitar = guitarRepository.findById(guitar.getGuitarId());

        assertFalse(deletedGuitar.isPresent());


    }
}
