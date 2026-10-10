package io.bootify.my_app.train_stop;

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
@RequestMapping(value = "/api/trainStops", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainStopResource {

    private final TrainStopService trainStopService;

    public TrainStopResource(final TrainStopService trainStopService) {
        this.trainStopService = trainStopService;
    }

    @GetMapping
    public ResponseEntity<List<TrainStopDTO>> getAllTrainStops() {
        return ResponseEntity.ok(trainStopService.findAll());
    }

    @GetMapping("/{seq}")
    public ResponseEntity<TrainStopDTO> getTrainStop(@PathVariable(name = "seq") final String seq) {
        return ResponseEntity.ok(trainStopService.get(seq));
    }

    @PostMapping
    public ResponseEntity<String> createTrainStop(
            @RequestBody @Valid final TrainStopDTO trainStopDTO) {
        final String createdSeq = trainStopService.create(trainStopDTO);
        return new ResponseEntity<>('"' + createdSeq + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{seq}")
    public ResponseEntity<String> updateTrainStop(@PathVariable(name = "seq") final String seq,
            @RequestBody @Valid final TrainStopDTO trainStopDTO) {
        trainStopService.update(seq, trainStopDTO);
        return ResponseEntity.ok('"' + seq + '"');
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteTrainStop(@PathVariable(name = "seq") final String seq) {
        trainStopService.delete(seq);
        return ResponseEntity.noContent().build();
    }

}
