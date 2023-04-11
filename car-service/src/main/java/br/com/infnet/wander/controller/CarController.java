package br.com.infnet.wander.controller;

import br.com.infnet.wander.domain.dto.CarRequest;
import br.com.infnet.wander.model.ApiError;
import br.com.infnet.wander.model.Car;
import br.com.infnet.wander.model.CarStatus;
import br.com.infnet.wander.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@Validated
@Tag(name = "car", description = "the car API")
@RestController
@RequestMapping("/api/v1")
public class CarController {

    @Autowired
    private CarService carService;
    

    
    /**
     * POST /car : Create a new car
     *
     * @param carRequest New car data (required)
     * @return Successfully created (status code 201)
     * or Invalid request message (status code 400)
     */
    @Operation(operationId = "createCar", summary = "Create a new car", tags = {"car"}, responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request message", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})},
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(value = "/car", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Car> createCar(
            @Parameter(name = "Car", description = "New car data", required = true) @Valid @RequestBody
                    CarRequest carRequest) {
        log.debug("Create Car Controller: [{}]", carRequest);
        return new ResponseEntity<>(carService.createCar(carRequest), HttpStatus.CREATED);
    }
    
    /**
     * DELETE /car/{id} : Delete a car by id
     *
     * @param id The id of the user to update (required)
     * @return Successfully deleted (status code 200)
     * or Invalid Car Id (status code 400)
     * or Car not found (status code 404)
     */
    @Operation(operationId = "deleteCarById", summary = "Delete a car by id", tags = {"car"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Car Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @DeleteMapping(value = "/car/{id}", produces = {"application/json"})
    public ResponseEntity<Response> deleteCarById(
            @Parameter(name = "id", description = "The id of the car to delete", required = true) @PathVariable("id")
                    Long id) {
        if(!carService.getCarById(id).getCarStatus().equals(CarStatus.RENTED)){
            carService.deleteCarById(id);
            Response response = new Response();

            response.setMessage("Successfully deleted car with id "+ id);
            return ResponseEntity.ok(response);
        }else{
            Response response = new Response();

            response.setMessage(String.format("The car with id %s cannot be deleted because the status is rented", id));
            return ResponseEntity.ok(response);
        }



    }
    
    /**
     * GET /car/{id} : Get a car by id
     *
     * @param id The id of the car to retrieve (required)
     * @return Successful Operation (status code 200)
     * or Invalid ID supplied (status code 400)
     * or Car not found (status code 404)
     */
    @Operation(operationId = "getCarById", summary = "Get a car by id", tags = {"car"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Car Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping(value = "/car/{id}", produces = {"application/json"})
    public ResponseEntity<Car> getCarById(
            @Parameter(name = "id", description = "The id of the car to retrieve", required = true) @PathVariable("id")
                    Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
        
    }
    
    /**
     * GET /car/{id} : Get cars by status
     *
     * @param carStatus The status of the cars to retrieve (required)
     * @return Successful Operation (status code 200)
     * or Invalid ID supplied (status code 400)
     */
    @Operation(operationId = "getCarsByStatus", summary = "Get a car by status", tags = {"car"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Car.class)))}),
            @ApiResponse(responseCode = "204", description = "Successful Operation but no content found"),
            @ApiResponse(responseCode = "400", description = "Invalid car status value", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping(value = "/car/findByStatus", produces = {"application/json"})
    public ResponseEntity<List<Car>> getCarsByStatus(@RequestParam CarStatus carStatus) {
        List<Car> cars = carService.getCarsByStatus(carStatus);
        return cars.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cars);
    }
    
    /**
     * GET /car : Get a list of cars
     *
     * @return Successful Operation (status code 200)
     * or empty List when no Content found (status code 204)
     */
    @Operation(operationId = "getCars", summary = "Get a list of cars", tags = {"car"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Car.class)))}),
            @ApiResponse(responseCode = "204", description = "Successful Operation but no content found")})
    @GetMapping(value = "/car", produces = {"application/json"})
    public ResponseEntity<List<Car>> getCars() {
        List<Car> cars = carService.getCars();
        return cars.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cars);
    }
    
    /**
     * PUT /car/{id} : Update car by id
     *
     * @param id         The id of the car to update (required)
     * @param carRequest Updated car object (required)
     * @return Successful operation (status code 200)
     * or Invalid ID supplied (status code 400)
     * or Car not found (status code 404)
     * or Validation exception (status code 405)
     */
    @Operation(operationId = "updateCarById", summary = "Update car by id", tags = {"car"}, responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Car Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "405", description = "Validation exception", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})},
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping(value = "/car/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Car> updateCarById(
            @Parameter(name = "id", description = "The id of the car to update", required = true) @PathVariable("id")
                    Long id,
            @Parameter(name = "carRequest", description = "Updated car object", required = true) @Valid @RequestBody
            CarRequest carRequest) {
        return ResponseEntity.ok(carService.updateCarById(id, carRequest));
    }
}
