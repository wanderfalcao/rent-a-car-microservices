package br.com.infnet.wander.repository;

import br.com.infnet.wander.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, Long> {


}
