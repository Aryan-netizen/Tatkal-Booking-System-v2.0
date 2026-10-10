package io.bootify.my_app.berth;

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
@RequestMapping(value = "/api/berths", produces = MediaType.APPLICATION_JSON_VALUE)
public class BerthResource {

    private final BerthService berthService;

    public BerthResource(final BerthService berthService) {
        this.berthService = berthService;
    }

    @GetMapping
    public ResponseEntity<List<BerthDTO>> getAllBerths() {
        return ResponseEntity.ok(berthService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BerthDTO> getBerth(@PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(berthService.get(id));
    }

    @PostMapping
    public ResponseEntity<String> createBerth(@RequestBody @Valid final BerthDTO berthDTO) {
        final String createdId = berthService.create(berthDTO);
        return new ResponseEntity<>('"' + createdId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBerth(@PathVariable(name = "id") final String id,
            @RequestBody @Valid final BerthDTO berthDTO) {
        berthService.update(id, berthDTO);
        return ResponseEntity.ok('"' + id + '"');
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBerth(@PathVariable(name = "id") final String id) {
        berthService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
