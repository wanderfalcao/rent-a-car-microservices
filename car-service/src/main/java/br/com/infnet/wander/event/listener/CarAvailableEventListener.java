package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.event.CarAvailableEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Log4j2
@Component
public class CarAvailableEventListener {
    
    private final RabbitTemplate rabbitTemplate;
    private final String queueCarAvailable;
    private final ObjectMapper mapper;
    
    public CarAvailableEventListener(RabbitTemplate rabbitTemplate,
                                     @Value("${queue.car-available}") String queueCarAvailable, ObjectMapper mapper) {
        
        this.rabbitTemplate = rabbitTemplate;
        this.queueCarAvailable = queueCarAvailable;
        this.mapper = mapper;
        
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(CarAvailableEvent event) throws JsonProcessingException {
        
        log.info("Sending car available event to {}, event: {}", queueCarAvailable, event);
        
        rabbitTemplate.convertAndSend(queueCarAvailable, mapper.writeValueAsString(event));
        
    }
}
