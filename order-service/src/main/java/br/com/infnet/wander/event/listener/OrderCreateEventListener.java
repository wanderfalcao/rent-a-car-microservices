package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.event.OrderCreateEvent;
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
public class OrderCreateEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final String queueOrderCreate;
    private final ObjectMapper mapper;

    public OrderCreateEventListener(RabbitTemplate rabbitTemplate,
                                    @Value("${queue.order-create}") String queueOrderCreate, ObjectMapper mapper) {

        this.rabbitTemplate = rabbitTemplate;
        this.queueOrderCreate = queueOrderCreate;
        this.mapper = mapper;

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(OrderCreateEvent event) throws JsonProcessingException {

        log.info("Sending order create event to {}, event: {}", queueOrderCreate, event);

        rabbitTemplate.convertAndSend(queueOrderCreate, mapper.writeValueAsString(event));

    }
}
