package br.com.infnet.wander.service;

import br.com.infnet.wander.domain.dto.CarRequest;
import br.com.infnet.wander.domain.exception.CarNotFoundException;
import br.com.infnet.wander.domain.mapper.CarMapper;
import br.com.infnet.wander.event.CarUnavailableEvent;
import br.com.infnet.wander.event.OrderStatusFailureEvent;
import br.com.infnet.wander.event.OrderStatusSuccessEvent;
import br.com.infnet.wander.model.Car;
import br.com.infnet.wander.model.CarStatus;
import br.com.infnet.wander.event.CarAvailableEvent;
import br.com.infnet.wander.model.Order;
import br.com.infnet.wander.model.OrderStatus;
import br.com.infnet.wander.repository.CarRepository;
import br.com.infnet.wander.utility.TransactionIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final ApplicationEventPublisher publisher;
    private final TransactionIdentifier transactionId;
    private final SequenceGeneratorCarService sequenceGeneratorCarService;

    public Car createCar(CarRequest carRequest) {

        Car car = carMapper.create(carRequest);
        car.setId(sequenceGeneratorCarService.generateSequence(Car.SEQUENCE_NAME));
        log.info("Service Mapped Car: [{}]", car);

        Car savedCar = carRepository.save(car);
        log.info("Saved Car: [{}]", savedCar);

        return savedCar;
    }

    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with id " + id + " could not be found"));
    }

    public List<Car> getCars() {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().iterator().forEachRemaining(cars::add);
        return cars;
    }


    public List<Car> getCarsByStatus(CarStatus carStatus) {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().iterator().forEachRemaining(cars::add);
        return cars.stream().filter(car -> car.getCarStatus() == carStatus).toList();
    }

    public Car updateCarById(Long id, CarRequest carRequest) {
        return carRepository.save(carMapper.update(carRequest,
                carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car was not found for " + id))));
    }

    public void checkCar(Order order) {

        log.info("Check for car id {}", order.getCarId());

        if (
                carRepository.findAll()
                        .stream()
                        .anyMatch(car -> car.getId()
                                .equals(order.getCarId()))
        ) {

            carRepository.findById(order.getCarId()).ifPresent(car -> {
                order.setCarId(car.getId());
                publishCarAvailable(order);
            });

        } else {
            publishCarUnavailable(order);
        }
    }

    public void updateStatusCar(Order order, OrderStatus oldOrderStatus) {
        
        try {

            Car car = carRepository.findById(order.getCarId())
                    .orElseThrow(() -> new CarNotFoundException("Car was not found for " + order.getCarId()));


            if (order.getOrderStatus() == OrderStatus.ACTIVE) {
                car.setCarStatus(CarStatus.RENTED);
            } else {
                car.setCarStatus(CarStatus.AVAILABLE);

            }

            log.info("Update car status  {}", car);

            carRepository.save(car);

            order.setCarId(car.getId());

            publishOrderStatusSuccess(order);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            publishOrderStatusFailure(order, oldOrderStatus);
        }
    }

    private void publishCarAvailable(Order order) {

        CarAvailableEvent event = new CarAvailableEvent(transactionId.getTransactionId(), order);

        log.info("Publishing car available event {}", event);

        publisher.publishEvent(event);

    }

    private void publishCarUnavailable(Order order) {

        CarUnavailableEvent event = new CarUnavailableEvent(transactionId.getTransactionId(), order);

        log.info("Publishing car unavailable event {}", event);

        publisher.publishEvent(event);

    }

    private void publishOrderStatusSuccess(Order order) {

        OrderStatusSuccessEvent event = new OrderStatusSuccessEvent(transactionId.getTransactionId(), order);

        log.info("Publishing order status success event {}", event);

        publisher.publishEvent(event);

    }

    private void publishOrderStatusFailure(Order order, OrderStatus oldOrderStatus) {

        OrderStatusFailureEvent event = new OrderStatusFailureEvent(transactionId.getTransactionId(), order, oldOrderStatus);

        log.info("Publishing order status failure event {}", event);

        publisher.publishEvent(event);

    }


}



