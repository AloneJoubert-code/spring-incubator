package entelect.training.incubator.spring.flight.controller;

import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.flight.model.FlightsSearchRequest;
import entelect.training.incubator.spring.flight.service.FlightsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flights")
@Tag(name = "Flights", description = "Flight management API")
public class FlightsController {

    private final Logger LOGGER = LoggerFactory.getLogger(FlightsController.class);

    private final FlightsService flightsService;

    public FlightsController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @Operation(summary = "Create a new flight", description = "Requires ADMIN role")
    @PostMapping
    public ResponseEntity<?> createFlight(@RequestBody Flight flight) {
        LOGGER.info("Processing flight creation request for flight={id}", flight);

        final Flight savedFlight = flightsService.createFlight(flight);

        LOGGER.trace("Flight created");
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all flights", description = "Public — no authentication required")
    @GetMapping()
    public ResponseEntity<?> getFlights() {
        LOGGER.info("Fetching all flights");
        List<Flight> flights = this.flightsService.getFlights();

        if (!flights.isEmpty()) {
            LOGGER.trace("Found flights");
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }

        LOGGER.trace("No flights found");
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get a flight by ID", description = "Public — no authentication required")
    @GetMapping("{id}")
    public ResponseEntity<?> getFlightById(@PathVariable Integer id) {
        LOGGER.info("Processing flight search request for flight id={}", id);
        Flight flight = this.flightsService.getFlight(id);

        if (flight != null) {
            LOGGER.trace("Found flight");
            return new ResponseEntity<>(flight, HttpStatus.OK);
        }

        LOGGER.trace("Flight not found");
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Search flights", description = "Search by departure time, days to departure, or origin/destination. Public access")
    @PostMapping("/search")
    public ResponseEntity<?> searchFlights(@RequestBody FlightsSearchRequest searchRequest) {
        LOGGER.info("Processing flight search request: {}", searchRequest);

        List<Flight> flights = flightsService.searchFlights(searchRequest);

        if (!flights.isEmpty()) {
            LOGGER.trace("Found flights: {}", flights);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }

        LOGGER.trace("No flights found");
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get flight specials", description = "Returns discounted flights for loyalty members. Requires LOYALTY_USER role")
    @GetMapping("/specials")
    public List<Flight> getFlightSpecials() {
        LOGGER.info("Processing flight specials request");

        List<Flight> discountedFlights = flightsService.getDiscountedFlights();

        LOGGER.trace("Flight specials: {}", discountedFlights);
        return discountedFlights;
    }
}
