package br.com.infnet.wander.event.handler;

import br.com.infnet.wander.domain.dto.CarRequest;
import br.com.infnet.wander.domain.exception.CarNotFoundException;
import br.com.infnet.wander.model.Car;
import br.com.infnet.wander.repository.CarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OrderGetCarEventhandler {
    private final CarRepository carRepository;
    private final RabbitTemplate rabbitTemplate;
    private final String queueSendCar;

    private final ObjectMapper mapper;

    public OrderGetCarEventhandler(CarRepository carRepository, RabbitTemplate rabbitTemplate,
                                   @Value("${queue.car-get-by-id}") String queueSendCar, ObjectMapper mapper) {
        this.carRepository = carRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.queueSendCar = queueSendCar;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${queue.car-get-by-id}")
    public void getCarById(String carId, Message message) throws JsonProcessingException {
        log.info("Received event to get car with id: {}", carId);

        Long id = Long.parseLong(carId);
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with id " + id + " not found"));

        CarRequest carDto = new CarRequest(car.getCarStatus(), car.getManufacturer(), car.getColor(), car.getModel(), car.getPrice(), car.getPictureLink());

//        MessageProperties messageProperties = message.getMessageProperties();
//        Message responseMessage = rabbitTemplate.sendAndReceive(queueSendCar,
//                mapper.writeValueAsString(carDto),
//                messageProperties);

        MessageProperties props = new MessageProperties();
        props.setHeader("custom-header", "value");

        String payload = mapper.writeValueAsString(carDto);

        Message messageReturn = new Message(payload.getBytes(), props);

        Message response = rabbitTemplate.sendAndReceive(queueSendCar, messageReturn);

        log.info("Response message: {}", response);
    }
}
