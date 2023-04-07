package br.com.infnet.wander.event;

import br.com.infnet.wander.domain.dto.CarDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class CarResponseEvent {
    private String transactionId;

    private CarDTO car;
}

