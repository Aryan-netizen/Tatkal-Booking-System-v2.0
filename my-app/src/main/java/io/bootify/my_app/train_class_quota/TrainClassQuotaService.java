package io.bootify.my_app.train_class_quota;

import io.bootify.my_app.events.BeforeDeleteQuota;
import io.bootify.my_app.events.BeforeDeleteTrain;
import io.bootify.my_app.events.BeforeDeleteTravelClass;
import io.bootify.my_app.quota.Quota;
import io.bootify.my_app.quota.QuotaRepository;
import io.bootify.my_app.train.Train;
import io.bootify.my_app.train.TrainRepository;
import io.bootify.my_app.travel_class.TravelClass;
import io.bootify.my_app.travel_class.TravelClassRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TrainClassQuotaService {

    private final TrainClassQuotaRepository trainClassQuotaRepository;
    private final TrainRepository trainRepository;
    private final TravelClassRepository travelClassRepository;
    private final QuotaRepository quotaRepository;

    public TrainClassQuotaService(final TrainClassQuotaRepository trainClassQuotaRepository,
            final TrainRepository trainRepository,
            final TravelClassRepository travelClassRepository,
            final QuotaRepository quotaRepository) {
        this.trainClassQuotaRepository = trainClassQuotaRepository;
        this.trainRepository = trainRepository;
        this.travelClassRepository = travelClassRepository;
        this.quotaRepository = quotaRepository;
    }

    public List<TrainClassQuotaDTO> findAll() {
        final List<TrainClassQuota> trainClassQuotas = trainClassQuotaRepository.findAll(Sort.by("seats"));
        return trainClassQuotas.stream()
                .map(trainClassQuota -> mapToDTO(trainClassQuota, new TrainClassQuotaDTO()))
                .toList();
    }

    public TrainClassQuotaDTO get(final String seats) {
        return trainClassQuotaRepository.findById(seats)
                .map(trainClassQuota -> mapToDTO(trainClassQuota, new TrainClassQuotaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TrainClassQuotaDTO trainClassQuotaDTO) {
        final TrainClassQuota trainClassQuota = new TrainClassQuota();
        mapToEntity(trainClassQuotaDTO, trainClassQuota);
        trainClassQuota.setSeats(trainClassQuotaDTO.getSeats());
        return trainClassQuotaRepository.save(trainClassQuota).getSeats();
    }

    public void update(final String seats, final TrainClassQuotaDTO trainClassQuotaDTO) {
        final TrainClassQuota trainClassQuota = trainClassQuotaRepository.findById(seats)
                .orElseThrow(NotFoundException::new);
        mapToEntity(trainClassQuotaDTO, trainClassQuota);
        trainClassQuotaRepository.save(trainClassQuota);
    }

    public void delete(final String seats) {
        final TrainClassQuota trainClassQuota = trainClassQuotaRepository.findById(seats)
                .orElseThrow(NotFoundException::new);
        trainClassQuotaRepository.delete(trainClassQuota);
    }

    private TrainClassQuotaDTO mapToDTO(final TrainClassQuota trainClassQuota,
            final TrainClassQuotaDTO trainClassQuotaDTO) {
        trainClassQuotaDTO.setSeats(trainClassQuota.getSeats());
        trainClassQuotaDTO.setTrainNumber(trainClassQuota.getTrainNumber() == null ? null : trainClassQuota.getTrainNumber().getNumber());
        trainClassQuotaDTO.setClassCode(trainClassQuota.getClassCode() == null ? null : trainClassQuota.getClassCode().getCode());
        trainClassQuotaDTO.setQuota(trainClassQuota.getQuota() == null ? null : trainClassQuota.getQuota().getCode());
        return trainClassQuotaDTO;
    }

    private TrainClassQuota mapToEntity(final TrainClassQuotaDTO trainClassQuotaDTO,
            final TrainClassQuota trainClassQuota) {
        final Train trainNumber = trainClassQuotaDTO.getTrainNumber() == null ? null : trainRepository.findById(trainClassQuotaDTO.getTrainNumber())
                .orElseThrow(() -> new NotFoundException("trainNumber not found"));
        trainClassQuota.setTrainNumber(trainNumber);
        final TravelClass classCode = trainClassQuotaDTO.getClassCode() == null ? null : travelClassRepository.findById(trainClassQuotaDTO.getClassCode())
                .orElseThrow(() -> new NotFoundException("classCode not found"));
        trainClassQuota.setClassCode(classCode);
        final Quota quota = trainClassQuotaDTO.getQuota() == null ? null : quotaRepository.findById(trainClassQuotaDTO.getQuota())
                .orElseThrow(() -> new NotFoundException("quota not found"));
        trainClassQuota.setQuota(quota);
        return trainClassQuota;
    }

    public boolean seatsExists(final String seats) {
        return trainClassQuotaRepository.existsBySeatsIgnoreCase(seats);
    }

    @EventListener(BeforeDeleteTrain.class)
    public void on(final BeforeDeleteTrain event) {
        final ReferencedException referencedException = new ReferencedException();
        final TrainClassQuota trainNumberTrainClassQuota = trainClassQuotaRepository.findFirstByTrainNumberNumber(event.getNumber());
        if (trainNumberTrainClassQuota != null) {
            referencedException.setKey("train.trainClassQuota.trainNumber.referenced");
            referencedException.addParam(trainNumberTrainClassQuota.getSeats());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTravelClass.class)
    public void on(final BeforeDeleteTravelClass event) {
        final ReferencedException referencedException = new ReferencedException();
        final TrainClassQuota classCodeTrainClassQuota = trainClassQuotaRepository.findFirstByClassCodeCode(event.getCode());
        if (classCodeTrainClassQuota != null) {
            referencedException.setKey("travelClass.trainClassQuota.classCode.referenced");
            referencedException.addParam(classCodeTrainClassQuota.getSeats());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteQuota.class)
    public void on(final BeforeDeleteQuota event) {
        final ReferencedException referencedException = new ReferencedException();
        final TrainClassQuota quotaTrainClassQuota = trainClassQuotaRepository.findFirstByQuotaCode(event.getCode());
        if (quotaTrainClassQuota != null) {
            referencedException.setKey("quota.trainClassQuota.quota.referenced");
            referencedException.addParam(quotaTrainClassQuota.getSeats());
            throw referencedException;
        }
    }

}
