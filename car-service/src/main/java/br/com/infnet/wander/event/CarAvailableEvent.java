package br.com.infnet.wander.event;


import br.com.infnet.wander.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CarAvailableEvent {

    private String transactionId;

    private Order order;
}
