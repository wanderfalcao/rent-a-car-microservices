package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.event.OrderUpdateEvent;
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
public class OrderUpdateEventListener {
    
    private final RabbitTemplate rabbitTemplate;
    private final String queueOrderUpdate;
    private final ObjectMapper mapper;
    
    public OrderUpdateEventListener(RabbitTemplate rabbitTemplate,
                                    @Value("${queue.order-update}") String queueOrderUpdate, ObjectMapper mapper) {
        
        this.rabbitTemplate = rabbitTemplate;
        this.queueOrderUpdate = queueOrderUpdate;
        this.mapper = mapper;
        
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(OrderUpdateEvent event) throws JsonProcessingException {
        
        log.info("Sending order update event to {}, event: {}", queueOrderUpdate, event);
        
        rabbitTemplate.convertAndSend(queueOrderUpdate, mapper.writeValueAsString(event));
        
    }
}
