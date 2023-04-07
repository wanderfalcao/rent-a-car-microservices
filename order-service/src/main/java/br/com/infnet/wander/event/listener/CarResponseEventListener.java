package br.com.infnet.wander.event.listener;

import br.com.infnet.wander.domain.dto.CarDTO;
import br.com.infnet.wander.event.CarRequestEvent;
import br.com.infnet.wander.event.CarResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
public class CarResponseEventListener {
    private final RabbitTemplate rabbitTemplate;
    private final String queueSendIdToGetCar;
    private final ObjectMapper mapper;
    @Autowired
    private ApplicationEventPublisher publisher;

    public CarResponseEventListener(RabbitTemplate rabbitTemplate,
                                    @Value("${queue.car-get-by-id}") String queueSendIdToGetCar,
                                    ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueSendIdToGetCar = queueSendIdToGetCar;
        this.mapper = mapper;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onGetByIdCarEvent(CarRequestEvent event) throws IOException {

        log.info("Sending id to get Car event to {}, event: {}", queueSendIdToGetCar, event);


        Message message = MessageBuilder
                .withBody(mapper.writeValueAsBytes(event))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setCorrelationId(event.getTransactionId())
                .build();

        Message replyMessage = rabbitTemplate.sendAndReceive(queueSendIdToGetCar, message);

        if (replyMessage != null && replyMessage.getBody() != null) {
            CarDTO car = mapper.readValue(replyMessage.getBody(), CarDTO.class);
            CarResponseEvent carResponseEvent = new CarResponseEvent(event.getTransactionId(), car);
            publisher.publishEvent(carResponseEvent);
        }
    }
}
