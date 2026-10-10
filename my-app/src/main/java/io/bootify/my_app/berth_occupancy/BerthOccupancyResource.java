package io.bootify.my_app.berth_occupancy;

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
@RequestMapping(value = "/api/berthOccupancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class BerthOccupancyResource {

    private final BerthOccupancyService berthOccupancyService;

    public BerthOccupancyResource(final BerthOccupancyService berthOccupancyService) {
        this.berthOccupancyService = berthOccupancyService;
    }

    @GetMapping
    public ResponseEntity<List<BerthOccupancyDTO>> getAllBerthOccupancies() {
        return ResponseEntity.ok(berthOccupancyService.findAll());
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<BerthOccupancyDTO> getBerthOccupancy(
            @PathVariable(name = "tripId") final String tripId) {
        return ResponseEntity.ok(berthOccupancyService.get(tripId));
    }

    @PostMapping
    public ResponseEntity<String> createBerthOccupancy(
            @RequestBody @Valid final BerthOccupancyDTO berthOccupancyDTO) {
        final String createdTripId = berthOccupancyService.create(berthOccupancyDTO);
        return new ResponseEntity<>('"' + createdTripId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<String> updateBerthOccupancy(
            @PathVariable(name = "tripId") final String tripId,
            @RequestBody @Valid final BerthOccupancyDTO berthOccupancyDTO) {
        berthOccupancyService.update(tripId, berthOccupancyDTO);
        return ResponseEntity.ok('"' + tripId + '"');
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteBerthOccupancy(
            @PathVariable(name = "tripId") final String tripId) {
        berthOccupancyService.delete(tripId);
        return ResponseEntity.noContent().build();
    }

}
