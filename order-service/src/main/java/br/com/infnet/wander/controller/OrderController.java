package br.com.infnet.wander.controller;


import br.com.infnet.wander.domain.dto.OrderRequest;
import br.com.infnet.wander.domain.dto.OrderResponse;
import br.com.infnet.wander.model.ApiError;
import br.com.infnet.wander.model.Order;
import br.com.infnet.wander.model.OrderStatus;
import br.com.infnet.wander.model.Response;
import br.com.infnet.wander.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@Tag(name = "order", description = "the order API")
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /order : Create a new order
     *
     * @param orderRequest New order object (required)
     * @return Successfully created (status code 201)
     * or Invalid request message (status code 400)
     */
    @Operation(operationId = "createOrder", summary = "Create a new order", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request message", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @PostMapping(value = "/order", produces = {"application/json"}, consumes = {"application/json"})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Order> createOrder(
            @Parameter(name = "Order", description = "New order object", required = true) @Valid @RequestBody
            OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    /**
     * DELETE /order/{id} : Delete an order by id
     *
     * @param id The id of the order to update (required)
     * @return Successfully deleted (status code 200)
     * or Invalid Order Id (status code 400)
     * or Order not found (status code 404)
     */
    @Operation(operationId = "deleteOrderById", summary = "Delete an order by id", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Order Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/order/{id}", produces = {"application/json"})
    public ResponseEntity<Response> deleteOrderById(
            @Parameter(name = "id", description = "The id of the order to update", required = true) @PathVariable("id")
            Long id) {
        orderService.deleteOrderById(id);
        Response response = new Response();
        response.setMessage(String.format("Successfully deleted Order with id %s", id));
        return ResponseEntity.ok(response);
    }


    /**
     * GET /order : Get all orders
     *
     * @return Successful Operation (status code 200)
     */
    @Operation(operationId = "getAllOrders", summary = "Get all orders", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Order.class)))})},
            security = {@SecurityRequirement(name = "bearerAuth")})
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/order/getAll", produces = {"application/json"})
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * GET /order/{id} : Get order by id
     *
     * @param id The id of the order to retrieve (required)
     * @return Successful Operation (status code 200)
     * or Invalid ID supplied (status code 400)
     * or Order not found (status code 404)
     */
    @Operation(operationId = "getOrderById", summary = "Get oder by id ", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Order Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping(value = "/order/{id}", produces = {"application/json"})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<OrderResponse> getOrderById(
            @Parameter(name = "id", description = "The id of the order to retrieve", required = true)
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    /**
     * PUT /order/{id} : Update order by Id
     *
     * @param id           The id of the order to update (required)
     * @param orderRequest Updated order object (required)
     * @return Successful operation (status code 200)
     * or Invalid ID supplied (status code 400)
     * or Order not found (status code 404)
     * or Validation exception (status code 405)
     */
    @Operation(operationId = "updateOrderById", summary = "Update order by Id", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Order Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "405", description = "Validation exception", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping(value = "/order/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Order> updateOrderById(
            @Parameter(name = "id", description = "The id of the order to update", required = true) @PathVariable("id")
            Long id,
            @Parameter(name = "order", description = "Updated order object", required = true) @Valid @RequestBody
            OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.updateOrderById(id, orderRequest));
    }

    @Operation(operationId = "updateStatusById", summary = "Update order + car status per Id", tags = {"order"},
            responses = {@ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
                    @ApiResponse(responseCode = "400", description = "Invalid Order Id", content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class))}),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class))})},
            security = {@SecurityRequirement(name = "bearerAuth")})
    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/order/{id}/{status}", produces = {"application/json"})
    public ResponseEntity<Order> updateStatusById(@PathVariable("id") Long id,
                                                  @PathVariable("status") OrderStatus orderStatus) {
        return ResponseEntity.ok(orderService.updateStatusById(id, orderStatus));
    }

}
