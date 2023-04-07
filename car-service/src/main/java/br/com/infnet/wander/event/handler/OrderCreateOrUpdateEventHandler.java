package br.com.infnet.wander.event.handler;


import br.com.infnet.wander.event.OrderCreateEvent;
import br.com.infnet.wander.event.OrderUpdateEvent;
import br.com.infnet.wander.event.OrderUpdateStatusEvent;
import br.com.infnet.wander.service.CarService;
import br.com.infnet.wander.utility.TransactionIdentifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@AllArgsConstructor
@Component
public class OrderCreateOrUpdateEventHandler {
    
    private final CarService carService;
    private ObjectMapper mapper;
    private final TransactionIdentifier transactionId;
    
    @Transactional
    @RabbitListener(queues = {"${queue.order-create}"})
    public void handleOrderCreate(@Payload String payload) throws JsonProcessingException {
        
        log.info("Handling an order create event {}", payload);

        OrderCreateEvent event = mapper.readValue(payload, OrderCreateEvent.class);

        transactionId.setTransactionId(event.getTransactionId());

        carService.checkCar(event.getOrder());
    }

    @Transactional
    @RabbitListener(queues = {"${queue.order-update}"})
    public void handleOrderUpdate(@Payload String payload) throws JsonProcessingException {

        log.info("Handling an order update event {}", payload);

        OrderUpdateEvent event = mapper.readValue(payload, OrderUpdateEvent.class);

        transactionId.setTransactionId(event.getTransactionId());

        carService.checkCar(event.getOrder());
    }


    @Transactional
    @RabbitListener(queues = {"${queue.order-update-status}"})
    public void handleOrderUpdateStatus(@Payload String payload) throws JsonProcessingException {

        log.info("Handling an order update status event {}", payload);

        OrderUpdateStatusEvent event = mapper.readValue(payload, OrderUpdateStatusEvent.class);

        transactionId.setTransactionId(event.getTransactionId());

        carService.updateStatusCar(event.getOrder(), event.getOrderOldStatus());
    }

}
