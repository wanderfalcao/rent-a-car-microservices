package br.com.infnet.wander.domain.dto;

import br.com.infnet.wander.model.OrderStatus;
import br.com.infnet.wander.model.Payment;
import br.com.infnet.wander.model.SagaStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    @Schema(name = "order_id", required = true)
    @JsonProperty("orderId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @Valid
    @JsonProperty("car_DTO")
    @Schema(name = "car_DTO", required = true)
    private CarDTO car;

    @Valid
    @Schema(name = "date_of_booking", required = true)
    @JsonProperty("date_of_booking")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBooking;

    @Valid
    @Schema(name = "date_of_rental", required = true)
    @JsonProperty("date_of_rental")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfRental;

    @Valid
    @Schema(name = "date_of_return", required = true)
    @JsonProperty("date_of_return")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfReturn;

    @Valid
    @Schema(name = "method_of_payment", required = true)
    @JsonProperty("method_of_payment")
    private Payment payment;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 64)
    @Schema(name = "first_name", required = true)
    @JsonProperty("first_name")
    private String firstName;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 64)
    @Schema(name = "last_name", required = true)
    @JsonProperty("last_name")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Size(max = 64)
    @Schema(name = "email", required = true)
    @JsonProperty("email")
    private String email;

    @NotNull
    @Schema(name = "order_status", required = true)
    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @Schema(name = "saga_status")
    @JsonProperty("saga_status")
    private SagaStatus status;


}
