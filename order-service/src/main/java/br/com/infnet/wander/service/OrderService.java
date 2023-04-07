package br.com.infnet.wander.service;

import br.com.infnet.wander.domain.dto.CarDTO;
import br.com.infnet.wander.domain.dto.OrderRequest;
import br.com.infnet.wander.domain.exception.OrderNotFoundException;
import br.com.infnet.wander.domain.mapper.OrderMapper;
import br.com.infnet.wander.event.CarGetByIdEvent;
import br.com.infnet.wander.event.OrderCreateEvent;
import br.com.infnet.wander.event.OrderUpdateEvent;
import br.com.infnet.wander.event.OrderUpdateStatusEvent;
import br.com.infnet.wander.model.*;
import br.com.infnet.wander.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private  ApplicationEventPublisher publisher;
    @Autowired
    private  OrderMapper orderMapper;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    private String transactionId;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {

        Order order = orderMapper.create(orderRequest);
        order.setId(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME));
        order.setStatus(SagaStatus.CREATED);

//        CarDTO carDTO = new CarDTO();
//        carDTO.setId(orderRequest.getCarId());
//        order.setCarDTO(carDTO);

        log.info("Saving an order {}", order);

        Order returnOrder = orderRepository.save(order);

        publishCreate(returnOrder);

        return returnOrder;
    }
    
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().iterator().forEachRemaining(orders::add);
        return orders;
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id" + id + " could not be found, ok bye."));
    }

    @Transactional
    public CarDTO getCarById(Long id) {

        return publishGetCarById(id);
    }


    private CarDTO publishGetCarById(Long id) {

        transactionId = UUID.randomUUID().toString();
        CarGetByIdEvent event = new CarGetByIdEvent(transactionId, id, new CarDTO());

        log.info("Publishing an car id to get complete information of car event {}", event);

        publisher.publishEvent(event);
        CarDTO car = (CarDTO)event.getCar();
        return car;
    }
    @Async
    @EventListener
    public void onCarResponseEvent(CarGetByIdEvent event) {
        // Filtra eventos pelo transactionId correspondente
        if (this.transactionId.equals(event.getTransactionId())) {
            CarDTO car = event.getCar();
            // Utilize o CarDTO retornado aqui
        }
    }

    @Transactional
    public Order updateOrderById(Long id, OrderRequest orderRequest) {
        Order oldOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order was not found for " + id));
        Order updatedOrder = orderMapper.update(orderRequest, oldOrder);

        updatedOrder.setStatus(SagaStatus.CREATED);

        log.info("Updating an order {}", updatedOrder);

        Order returnOrder = orderRepository.save(updatedOrder);

        publishUpdate(returnOrder);

        return returnOrder;
    }
    
    public Order updateStatusById(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order was not found for " + id));
        OrderStatus oldOrderStatus = order.getOrderStatus();
        order.setOrderStatus(orderStatus);
        order.setStatus(SagaStatus.CREATED);

        log.info("Updating an order {}", order);

        Order updateOrder = orderRepository.save(order);

        publishUpdateStatus(updateOrder, oldOrderStatus);

        return updateOrder;
    }

    private void publishCreate(Order order) {

        OrderCreateEvent event = new OrderCreateEvent(UUID.randomUUID().toString(), order);

        log.info("Publishing an Order created event {}", event);

        publisher.publishEvent(event);

    }

    private void publishUpdate(Order order) {

        OrderUpdateEvent event = new OrderUpdateEvent(UUID.randomUUID().toString(), order);

        log.info("Publishing an Order updated event {}", event);

        publisher.publishEvent(event);

    }

    private void publishUpdateStatus(Order order, OrderStatus orderStatus) {

        OrderUpdateStatusEvent event = new OrderUpdateStatusEvent(UUID.randomUUID().toString(), order, orderStatus);

        log.info("Publishing an Order updated status event {}", event);

        publisher.publishEvent(event);

    }
    
    @Transactional
    public void updateOrderCarUnavailable(Order order) {

        log.info("Canceling Order because Car isn't available {}", order);

        Optional<Order> optionalOrder = orderRepository.findById(order.getId());

        if (optionalOrder.isPresent()) {

            Order updateOrder = optionalOrder.get();
            updateOrder.setStatus(SagaStatus.CAR_REJECTED);
            orderRepository.save(updateOrder);

            log.info("Order {} was canceled - CAR_REJECTED", updateOrder.getId());

        } else {

            log.info("Cannot find an order {}", order.getId());

        }

    }

    public void updateOrderStatusSuccess(Order order) {

        log.info("Updating Order {} to {}", order, SagaStatus.FINISHED);

        Optional<Order> optionalOrder = orderRepository.findById(order.getId());

        if (optionalOrder.isPresent()) {

            Order updateOrder = optionalOrder.get();
            updateOrder.setStatus(SagaStatus.FINISHED);

            orderRepository.save(updateOrder);

            log.info("Order updated status success {} done", updateOrder.getId());

        } else {

            log.error("Cannot update Order to status {}, Order {} not found - status success", SagaStatus.FINISHED, order);

        }
    }

    public void updateOrderStatusFailure(Order order, OrderStatus orderOldStatus) {
        log.info("Revert Order status{} to {}", order, SagaStatus.FINISHED);

        Optional<Order> optionalOrder = orderRepository.findById(order.getId());

        if (optionalOrder.isPresent()) {

            Order updateOrder = optionalOrder.get();
            updateOrder.setStatus(SagaStatus.FINISHED);
            updateOrder.setOrderStatus(orderOldStatus);
            orderRepository.save(updateOrder);

            log.info("Order updated status failure {} done", updateOrder.getId());

        } else {

            log.error("Cannot update Order to status {}, Order {} not found - status failure", SagaStatus.FINISHED, order);

        }
    }
}
