package io.bootify.my_app.station;

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
@RequestMapping(value = "/api/stations", produces = MediaType.APPLICATION_JSON_VALUE)
public class StationResource {

    private final StationService stationService;

    public StationResource(final StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<StationDTO>> getAllStations() {
        return ResponseEntity.ok(stationService.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<StationDTO> getStation(@PathVariable(name = "code") final String code) {
        return ResponseEntity.ok(stationService.get(code));
    }

    @PostMapping
    public ResponseEntity<String> createStation(@RequestBody @Valid final StationDTO stationDTO) {
        final String createdCode = stationService.create(stationDTO);
        return new ResponseEntity<>('"' + createdCode + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<String> updateStation(@PathVariable(name = "code") final String code,
            @RequestBody @Valid final StationDTO stationDTO) {
        stationService.update(code, stationDTO);
        return ResponseEntity.ok('"' + code + '"');
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteStation(@PathVariable(name = "code") final String code) {
        stationService.delete(code);
        return ResponseEntity.noContent().build();
    }

}
