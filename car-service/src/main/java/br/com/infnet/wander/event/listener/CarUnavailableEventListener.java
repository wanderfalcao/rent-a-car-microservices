package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.event.CarUnavailableEvent;
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
public class CarUnavailableEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final String queueCarUnavailable;
    private final ObjectMapper mapper;

    public CarUnavailableEventListener(RabbitTemplate rabbitTemplate,
                                     @Value("${queue.car-unavailable}") String queueCarUnavailable, ObjectMapper mapper) {

        this.rabbitTemplate = rabbitTemplate;
        this.queueCarUnavailable = queueCarUnavailable;
        this.mapper = mapper;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(CarUnavailableEvent event) throws JsonProcessingException {

        log.info("Sending car unavailable event to {}, event: {}", queueCarUnavailable, event);

        rabbitTemplate.convertAndSend(queueCarUnavailable, mapper.writeValueAsString(event));

    }
}
