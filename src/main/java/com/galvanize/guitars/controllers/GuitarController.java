package com.galvanize.guitars.controllers;

import com.galvanize.guitars.entities.Guitar;
import com.galvanize.guitars.services.GuitarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guitars")
public class GuitarController {


    private GuitarService guitarService;

    public GuitarController(GuitarService guitarService) {
        this.guitarService = guitarService;
    }

    @GetMapping("/all")
    public List<Guitar> getAllGuitars(){
        return guitarService.getAll();
    }

    @PostMapping
    public Guitar createGuitar(@RequestBody Guitar guitar){
        return guitarService.createGuitar(guitar);
    }

    @PutMapping("/{id}")
    public Guitar updateGuitar(@RequestBody Guitar guitar, @PathVariable Long id ){
        // Get the guitar that they want to update
        Guitar g = guitarService.getGuitarById(guitar.getGuitarId());
        // If the IDs match, perform the update.  You can do this here, or in the service.update() method
        if (guitar != null) {
            //Call the update on guitarService
            return guitarService.updateGuitar(guitar);
        }else{
            //Should probably return an error response of some kind.
            return null;
        }
    }

    @PatchMapping("/{id}")
    public Guitar patchGuitar(@PathVariable Long id, @RequestBody Guitar changes){
        Guitar g = guitarService.getGuitarById(id);

        // NOTE: StringUtils.isNotEmpty() comes from the apache commons-lang library (see build.gradle dependency)
        if (StringUtils.isNotEmpty(changes.getBrand()))
            g.setBrand(changes.getBrand());
        if (StringUtils.isNotEmpty(changes.getModel()))
            g.setModel(changes.getModel());
        if (changes.getStrings()>0 )
            g.setStrings(changes.getStrings());

        return guitarService.updateGuitar(g);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGuitar(@PathVariable Long id){
        if (guitarService.deleteGuitar(id))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.valueOf(500));
    }

}
