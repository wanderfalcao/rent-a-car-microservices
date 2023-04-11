package br.com.infnet.wander.service;

import br.com.infnet.wander.domain.dto.CarDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "car-service")

public interface CarFeignClientService {
    @GetMapping(value = "/api/v1/car/{id}")
    CarDTO getCarById(@Parameter(name = "id",
            description = "The id of the car to retrieve",
            required = true) @PathVariable("id")
                      Long id);
}
