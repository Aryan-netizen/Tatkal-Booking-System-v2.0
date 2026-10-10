package io.bootify.my_app.berth_allocation;

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
@RequestMapping(value = "/api/berthAllocations", produces = MediaType.APPLICATION_JSON_VALUE)
public class BerthAllocationResource {

    private final BerthAllocationService berthAllocationService;

    public BerthAllocationResource(final BerthAllocationService berthAllocationService) {
        this.berthAllocationService = berthAllocationService;
    }

    @GetMapping
    public ResponseEntity<List<BerthAllocationDTO>> getAllBerthAllocations() {
        return ResponseEntity.ok(berthAllocationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BerthAllocationDTO> getBerthAllocation(
            @PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(berthAllocationService.get(id));
    }

    @PostMapping
    public ResponseEntity<String> createBerthAllocation(
            @RequestBody @Valid final BerthAllocationDTO berthAllocationDTO) {
        final String createdId = berthAllocationService.create(berthAllocationDTO);
        return new ResponseEntity<>('"' + createdId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBerthAllocation(@PathVariable(name = "id") final String id,
            @RequestBody @Valid final BerthAllocationDTO berthAllocationDTO) {
        berthAllocationService.update(id, berthAllocationDTO);
        return ResponseEntity.ok('"' + id + '"');
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBerthAllocation(@PathVariable(name = "id") final String id) {
        berthAllocationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
