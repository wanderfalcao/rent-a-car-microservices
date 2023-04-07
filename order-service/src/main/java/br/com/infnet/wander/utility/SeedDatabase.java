package br.com.infnet.wander.utility;


import br.com.infnet.wander.domain.dto.CarDTO;
import br.com.infnet.wander.model.*;
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

//        OpeningHours openingHoursDatasetOne = new OpeningHours(1, "06.00 Uhr - 23.30 Uhr",
//                "08.00 Uhr - 23.30 Uhr",
//                "10.00 Uhr - 23.30 Uhr",
//                "11.00 Uhr - 23.30 Uhr",
//                "07.30 Uhr - 23.30 Uhr",
//                "06.00 Uhr - 20.00 Uhr",
//                "05.00 Uhr - 23.00 Uhr");
//
//        Location locationDatasetOne =
//                new Location(Long.valueOf(1), openingHoursDatasetOne, "Airport Rio de Janeiro(Galeão)", "Av. 20 de Janeiro", "0", "Rio de Janeiro",
//                        "faleconosco@riogaleao.com", "(21) 3004-6050",
//                        SagaStatus.FINISHED);



        Order orderDatasetOne = new Order(
                Long.valueOf(1),
                Long.valueOf(1),
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
