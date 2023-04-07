package br.com.infnet.wander.event;


import br.com.infnet.wander.model.Order;
import br.com.infnet.wander.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderStatusFailureEvent {
    private String transactionId;
    private Order order;
    private OrderStatus orderOldStatus;
}
