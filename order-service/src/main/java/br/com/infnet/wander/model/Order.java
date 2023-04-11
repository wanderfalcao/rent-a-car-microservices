package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Document
@NoArgsConstructor
public class Order {

    @Transient
    public static final String SEQUENCE_NAME = "orders_sequence";

    @Id
    @Schema(name = "order_id", required = true)
    @JsonProperty("orderId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Valid
    @JsonProperty("car_id")
    @Schema(name = "car_id", required = true)
    @NotNull
    private Long carId;

    @Valid
    @Schema(name = "date_of_booking", required = true)
    @JsonProperty("date_of_booking")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate dateOfBooking;

    @Valid
    @Schema(name = "date_of_rental", required = true)
    @JsonProperty("date_of_rental")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate dateOfRental;

    @Valid
    @Schema(name = "date_of_return", required = true)
    @JsonProperty("date_of_return")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate dateOfReturn;

    @Valid
    @Schema(name = "method_of_payment", required = true)
    @JsonProperty("method_of_payment")
    @NotNull
    private Payment payment;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 64)
    @Schema(name = "first_name", required = true)
    @JsonProperty("first_name")
    @NotNull
    private String firstName;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 64)
    @Schema(name = "last_name", required = true)
    @JsonProperty("last_name")
    @NotNull
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
    @NotNull
    private SagaStatus status;

}

