package br.com.infnet.wander.repository;


import br.com.infnet.wander.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, BigInteger> {
    Optional<Order> findByOrderIdAndLastNameAllIgnoreCase(BigInteger uuid, String lastName);

}
