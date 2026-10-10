package io.bootify.my_app.passenger;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerResource {

    private final PassengerService passengerService;

    public PassengerResource(final PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping
    public ResponseEntity<List<PassengerDTO>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> getPassenger(@PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(passengerService.get(id));
    }

    @PostMapping
    public ResponseEntity<String> createPassenger(
            @RequestBody @Valid final PassengerDTO passengerDTO) {
        final String createdId = passengerService.create(passengerDTO);
        return new ResponseEntity<>('"' + createdId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePassenger(@PathVariable(name = "id") final String id,
            @RequestBody @Valid final PassengerDTO passengerDTO) {
        passengerService.update(id, passengerDTO);
        return ResponseEntity.ok('"' + id + '"');
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable(name = "id") final String id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
