package com.galvanize.guitars.services;

import com.galvanize.guitars.entities.Guitar;
import com.galvanize.guitars.repositories.GuitarRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuitarServiceTests {

    @Mock
    GuitarRepository guitarRepository;


    GuitarService guitarService;

    @Before
    public void setUp() throws Exception {
        guitarService = new GuitarService(guitarRepository);
    }

    @Test
    public void getAllGuitars() {
        List<Guitar> guitars = new ArrayList<>();
        Guitar g = null;
        for (int i = 0; i < 10; i++) {
            g = new Guitar("Brand"+i, "Model"+i, 6);
            g.setGuitarId(Long.valueOf(i));
            guitars.add(g);
        }
//        guitarRepository.saveAll(guitars);

        when(guitarRepository.findAll()).thenReturn(guitars);

        List<Guitar> svcGuitars = guitarService.getAll();
        assertTrue(svcGuitars.size()>0);
        for(Guitar guitar : svcGuitars) {
            assertNotNull(guitar.getGuitarId());
        }

    }

    @Test
    public void getOneGuitar() {
        Guitar guitar = new Guitar("Guild", "D45Bld", 6);
        guitar.setGuitarId(Long.valueOf(99));
        when(guitarRepository.findById(anyLong())).thenReturn(Optional.of(guitar));

        Guitar newGuitar = guitarService.getGuitarById(guitar.getGuitarId());

        assertNotNull(newGuitar.getGuitarId());
    }

    @Test
    public void createGuitar() {
        Guitar guitar = new Guitar("Guild", "D45Bld", 6);
        guitar.setGuitarId(Long.valueOf(99));

        when(guitarRepository.save(any())).thenReturn(guitar);

        guitar = guitarService.createGuitar(guitar);

        assertNotNull(guitar.getGuitarId());
    }

    @Test
    public void deleteGuitar() {
        Guitar guitar = new Guitar("Guild", "D45Bld", 6);
        guitar.setGuitarId(Long.valueOf(99));

        when(guitarRepository.findById(anyLong())).thenReturn(Optional.of(guitar));

        boolean deleted = guitarService.deleteGuitar(Long.valueOf(99));

        assertTrue(deleted);

//        Optional<Guitar> deletedGuitar = guitarRepository.findById(guitar.getGuitarId());

//        assertFalse(deletedGuitar.isPresent());


    }
}
