package io.bootify.my_app.train_class_quota;

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
@RequestMapping(value = "/api/trainClassQuotas", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainClassQuotaResource {

    private final TrainClassQuotaService trainClassQuotaService;

    public TrainClassQuotaResource(final TrainClassQuotaService trainClassQuotaService) {
        this.trainClassQuotaService = trainClassQuotaService;
    }

    @GetMapping
    public ResponseEntity<List<TrainClassQuotaDTO>> getAllTrainClassQuotas() {
        return ResponseEntity.ok(trainClassQuotaService.findAll());
    }

    @GetMapping("/{seats}")
    public ResponseEntity<TrainClassQuotaDTO> getTrainClassQuota(
            @PathVariable(name = "seats") final String seats) {
        return ResponseEntity.ok(trainClassQuotaService.get(seats));
    }

    @PostMapping
    public ResponseEntity<String> createTrainClassQuota(
            @RequestBody @Valid final TrainClassQuotaDTO trainClassQuotaDTO) {
        final String createdSeats = trainClassQuotaService.create(trainClassQuotaDTO);
        return new ResponseEntity<>('"' + createdSeats + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{seats}")
    public ResponseEntity<String> updateTrainClassQuota(
            @PathVariable(name = "seats") final String seats,
            @RequestBody @Valid final TrainClassQuotaDTO trainClassQuotaDTO) {
        trainClassQuotaService.update(seats, trainClassQuotaDTO);
        return ResponseEntity.ok('"' + seats + '"');
    }

    @DeleteMapping("/{seats}")
    public ResponseEntity<Void> deleteTrainClassQuota(
            @PathVariable(name = "seats") final String seats) {
        trainClassQuotaService.delete(seats);
        return ResponseEntity.noContent().build();
    }

}
