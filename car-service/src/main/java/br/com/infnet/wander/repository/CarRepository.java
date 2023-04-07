package br.com.infnet.wander.repository;

import br.com.infnet.wander.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends MongoRepository<Car,Long> {
}
