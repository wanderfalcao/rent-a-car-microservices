package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.event.OrderUpdateStatusEvent;
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
public class OrderUpdateStatusEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final String queueOrderUpdateStatus;
    private final ObjectMapper mapper;

    public OrderUpdateStatusEventListener(RabbitTemplate rabbitTemplate,
                                    @Value("${queue.order-update-status}") String queueOrderUpdateStatus, ObjectMapper mapper) {

        this.rabbitTemplate = rabbitTemplate;
        this.queueOrderUpdateStatus = queueOrderUpdateStatus;
        this.mapper = mapper;

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUpdateStatusEvent(OrderUpdateStatusEvent event) throws JsonProcessingException {

        log.info("Sending order update status event to {}, event: {}", queueOrderUpdateStatus, event);

        rabbitTemplate.convertAndSend(queueOrderUpdateStatus, mapper.writeValueAsString(event));

    }
}
