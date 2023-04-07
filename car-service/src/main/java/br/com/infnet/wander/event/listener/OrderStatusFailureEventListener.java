package br.com.infnet.wander.event.listener;


import br.com.infnet.wander.event.OrderStatusFailureEvent;
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
public class OrderStatusFailureEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final String queueOrderStatusFailure;
    private final ObjectMapper mapper;

    public OrderStatusFailureEventListener(RabbitTemplate rabbitTemplate,
                                     @Value("${queue.order-status-failure}") String queueOrderStatusFailure, ObjectMapper mapper) {

        this.rabbitTemplate = rabbitTemplate;
        this.queueOrderStatusFailure = queueOrderStatusFailure;
        this.mapper = mapper;

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(OrderStatusFailureEvent event) throws JsonProcessingException {

        log.info("Sending car available event to {}, event: {}", queueOrderStatusFailure, event);

        rabbitTemplate.convertAndSend(queueOrderStatusFailure, mapper.writeValueAsString(event));

    }
}
