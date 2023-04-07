package br.com.infnet.wander.event.handler;


import br.com.infnet.wander.event.CarUnavailableEvent;
import br.com.infnet.wander.event.OrderStatusFailureEvent;
import br.com.infnet.wander.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class RabbitEventHandler {
    
    private final OrderService orderService;
    
    private ObjectMapper mapper;
    
    @RabbitListener(queues = {"${queue.car-unavailable}"})
    public void handleCarUnavailableEvent(@Payload String payload) throws JsonProcessingException {
        
        log.info("Handling a car unavailable event {}", payload);
        
        CarUnavailableEvent event = mapper.readValue(payload, CarUnavailableEvent.class);
        
        
        orderService.updateOrderCarUnavailable(event.getOrder());
        
    }

    @RabbitListener(queues = {"${queue.order-status-failure}"})
    public void handleOrderStatusFailureEvent(@Payload String payload) throws JsonProcessingException {

        log.info("Handling a order status failure event {}", payload);

        OrderStatusFailureEvent event = mapper.readValue(payload, OrderStatusFailureEvent.class);

        orderService.updateOrderStatusFailure(event.getOrder(), event.getOrderOldStatus());

    }
}
