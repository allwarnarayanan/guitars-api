package com.galvanize.guitars.repositories;

import com.galvanize.guitars.entities.Guitar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuitarRepository extends JpaRepository<Guitar, Long> {
}
