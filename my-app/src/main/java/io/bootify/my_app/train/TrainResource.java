package io.bootify.my_app.train;

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
@RequestMapping(value = "/api/trains", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainResource {

    private final TrainService trainService;

    public TrainResource(final TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.findAll());
    }

    @GetMapping("/{number}")
    public ResponseEntity<TrainDTO> getTrain(@PathVariable(name = "number") final String number) {
        return ResponseEntity.ok(trainService.get(number));
    }

    @PostMapping
    public ResponseEntity<String> createTrain(@RequestBody @Valid final TrainDTO trainDTO) {
        final String createdNumber = trainService.create(trainDTO);
        return new ResponseEntity<>('"' + createdNumber + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{number}")
    public ResponseEntity<String> updateTrain(@PathVariable(name = "number") final String number,
            @RequestBody @Valid final TrainDTO trainDTO) {
        trainService.update(number, trainDTO);
        return ResponseEntity.ok('"' + number + '"');
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Void> deleteTrain(@PathVariable(name = "number") final String number) {
        trainService.delete(number);
        return ResponseEntity.noContent().build();
    }

}
