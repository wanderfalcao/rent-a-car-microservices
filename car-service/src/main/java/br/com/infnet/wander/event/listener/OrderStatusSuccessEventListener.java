package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.event.OrderStatusSuccessEvent;
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
public class OrderStatusSuccessEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final String queueOrderStatusSuccess;
    private final ObjectMapper mapper;

    public OrderStatusSuccessEventListener(RabbitTemplate rabbitTemplate,
                                     @Value("${queue.order-status-success}") String queueOrderStatusSuccess, ObjectMapper mapper) {

        this.rabbitTemplate = rabbitTemplate;
        this.queueOrderStatusSuccess = queueOrderStatusSuccess;
        this.mapper = mapper;

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(OrderStatusSuccessEvent event) throws JsonProcessingException {

        log.info("Sending order changed successfully status  event to {}, event: {}", queueOrderStatusSuccess, event);

        rabbitTemplate.convertAndSend(queueOrderStatusSuccess, mapper.writeValueAsString(event));

    }
}
