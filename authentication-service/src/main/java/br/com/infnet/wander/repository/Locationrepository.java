package br.com.infnet.wander.repository;

import br.com.infnet.wander.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Locationrepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationId(Long id);

}
