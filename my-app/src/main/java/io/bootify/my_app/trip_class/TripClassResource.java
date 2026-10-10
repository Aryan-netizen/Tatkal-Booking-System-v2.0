package io.bootify.my_app.trip_class;

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
@RequestMapping(value = "/api/tripClasses", produces = MediaType.APPLICATION_JSON_VALUE)
public class TripClassResource {

    private final TripClassService tripClassService;

    public TripClassResource(final TripClassService tripClassService) {
        this.tripClassService = tripClassService;
    }

    @GetMapping
    public ResponseEntity<List<TripClassDTO>> getAllTripClasses() {
        return ResponseEntity.ok(tripClassService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripClassDTO> getTripClass(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tripClassService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createTripClass(
            @RequestBody @Valid final TripClassDTO tripClassDTO) {
        final Long createdId = tripClassService.create(tripClassDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTripClass(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TripClassDTO tripClassDTO) {
        tripClassService.update(id, tripClassDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTripClass(@PathVariable(name = "id") final Long id) {
        tripClassService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
