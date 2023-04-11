package br.com.infnet.wander.service;

import br.com.infnet.wander.domain.dto.CarDTO;
import br.com.infnet.wander.domain.dto.OrderRequest;
import br.com.infnet.wander.domain.dto.OrderResponse;
import br.com.infnet.wander.domain.exception.OrderNotFoundException;
import br.com.infnet.wander.domain.mapper.OrderMapper;
import br.com.infnet.wander.event.OrderCreateEvent;
import br.com.infnet.wander.event.OrderUpdateEvent;
import br.com.infnet.wander.event.OrderUpdateStatusEvent;
import br.com.infnet.wander.model.Order;
import br.com.infnet.wander.model.OrderStatus;
import br.com.infnet.wander.model.SagaStatus;
import br.com.infnet.wander.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private CarFeignClientService carFeignClientService;
    private String transactionId;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {

        Order order = orderMapper.create(orderRequest);
        order.setId(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME));
        order.setStatus(SagaStatus.CREATED);
        order.setOrderStatus(OrderStatus.CREATED);


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

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id" + id + " could not be found, ok bye."));
        CarDTO carById = carFeignClientService.getCarById(order.getCarId());
        OrderResponse orderResponse = new OrderResponse(order.getId(),
                carById,
                order.getDateOfBooking(),
                order.getDateOfRental(),
                order.getDateOfReturn(),
                order.getPayment(),
                order.getFirstName(),
                order.getLastName(),
                order.getEmail(),
                order.getOrderStatus(),
                order.getStatus());
        return orderResponse;
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
