package br.com.infnet.wander.utility;


import br.com.infnet.wander.model.Order;
import br.com.infnet.wander.model.OrderStatus;
import br.com.infnet.wander.model.Payment;
import br.com.infnet.wander.model.SagaStatus;
import br.com.infnet.wander.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeedDatabase implements CommandLineRunner {

    private final OrderRepository orderRepository;

    public SeedDatabase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        Order orderDatasetOne = new Order(
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now(),
                LocalDate.now(),
                Payment.VISA,
                "Wander",
                "Campelo",
                "wanderfalcao@hotmail.com",
                OrderStatus.CREATED,
                SagaStatus.CREATED);

        this.orderRepository.deleteAll();

        this.orderRepository.save(orderDatasetOne);

    }
}
