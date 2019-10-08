package com.galvanize.guitars.services;

import com.galvanize.guitars.entities.Guitar;
import com.galvanize.guitars.repositories.GuitarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuitarService {

    GuitarRepository guitarRepository;

    @Autowired
    public GuitarService(GuitarRepository guitarRepository) {
        this.guitarRepository = guitarRepository;
    }

    public List<Guitar> getAll() {
        return guitarRepository.findAll();
    }

    public Guitar getGuitarById(Long id){
        return guitarRepository.findById(id).get();
    }

    public Guitar createGuitar(Guitar guitar){
        return guitarRepository.save(guitar);
    }

    public boolean deleteGuitar(Long guitarId) {
        Optional<Guitar> g = guitarRepository.findById(guitarId);
        if(g.isPresent()) {
            guitarRepository.delete(g.get());
            return true;
        }else{
            return false;
        }
    }

    public Guitar updateGuitar(Guitar guitar) {
        //Note that this will fail if the guitar doesn't exist.  You should plan for that.
        return guitarRepository.save(guitar);
    }
}
