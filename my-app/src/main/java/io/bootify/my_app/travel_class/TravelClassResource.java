package io.bootify.my_app.travel_class;

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
@RequestMapping(value = "/api/travelClasses", produces = MediaType.APPLICATION_JSON_VALUE)
public class TravelClassResource {

    private final TravelClassService travelClassService;

    public TravelClassResource(final TravelClassService travelClassService) {
        this.travelClassService = travelClassService;
    }

    @GetMapping
    public ResponseEntity<List<TravelClassDTO>> getAllTravelClasses() {
        return ResponseEntity.ok(travelClassService.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<TravelClassDTO> getTravelClass(
            @PathVariable(name = "code") final String code) {
        return ResponseEntity.ok(travelClassService.get(code));
    }

    @PostMapping
    public ResponseEntity<String> createTravelClass(
            @RequestBody @Valid final TravelClassDTO travelClassDTO) {
        final String createdCode = travelClassService.create(travelClassDTO);
        return new ResponseEntity<>('"' + createdCode + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<String> updateTravelClass(@PathVariable(name = "code") final String code,
            @RequestBody @Valid final TravelClassDTO travelClassDTO) {
        travelClassService.update(code, travelClassDTO);
        return ResponseEntity.ok('"' + code + '"');
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteTravelClass(@PathVariable(name = "code") final String code) {
        travelClassService.delete(code);
        return ResponseEntity.noContent().build();
    }

}
