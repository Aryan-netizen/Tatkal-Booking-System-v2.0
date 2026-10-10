package io.bootify.my_app.segment_availability;

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
@RequestMapping(value = "/api/segmentAvailabilities", produces = MediaType.APPLICATION_JSON_VALUE)
public class SegmentAvailabilityResource {

    private final SegmentAvailabilityService segmentAvailabilityService;

    public SegmentAvailabilityResource(
            final SegmentAvailabilityService segmentAvailabilityService) {
        this.segmentAvailabilityService = segmentAvailabilityService;
    }

    @GetMapping
    public ResponseEntity<List<SegmentAvailabilityDTO>> getAllSegmentAvailabilities() {
        return ResponseEntity.ok(segmentAvailabilityService.findAll());
    }

    @GetMapping("/{classCode}")
    public ResponseEntity<SegmentAvailabilityDTO> getSegmentAvailability(
            @PathVariable(name = "classCode") final String classCode) {
        return ResponseEntity.ok(segmentAvailabilityService.get(classCode));
    }

    @PostMapping
    public ResponseEntity<String> createSegmentAvailability(
            @RequestBody @Valid final SegmentAvailabilityDTO segmentAvailabilityDTO) {
        final String createdClassCode = segmentAvailabilityService.create(segmentAvailabilityDTO);
        return new ResponseEntity<>('"' + createdClassCode + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{classCode}")
    public ResponseEntity<String> updateSegmentAvailability(
            @PathVariable(name = "classCode") final String classCode,
            @RequestBody @Valid final SegmentAvailabilityDTO segmentAvailabilityDTO) {
        segmentAvailabilityService.update(classCode, segmentAvailabilityDTO);
        return ResponseEntity.ok('"' + classCode + '"');
    }

    @DeleteMapping("/{classCode}")
    public ResponseEntity<Void> deleteSegmentAvailability(
            @PathVariable(name = "classCode") final String classCode) {
        segmentAvailabilityService.delete(classCode);
        return ResponseEntity.noContent().build();
    }

}
