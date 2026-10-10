package io.bootify.my_app.train_stop;

import io.bootify.my_app.events.BeforeDeleteStation;
import io.bootify.my_app.events.BeforeDeleteTrain;
import io.bootify.my_app.events.BeforeDeleteTrainStop;
import io.bootify.my_app.station.Station;
import io.bootify.my_app.station.StationRepository;
import io.bootify.my_app.train.Train;
import io.bootify.my_app.train.TrainRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TrainStopService {

    private final TrainStopRepository trainStopRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final ApplicationEventPublisher publisher;

    public TrainStopService(final TrainStopRepository trainStopRepository,
            final TrainRepository trainRepository, final StationRepository stationRepository,
            final ApplicationEventPublisher publisher) {
        this.trainStopRepository = trainStopRepository;
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.publisher = publisher;
    }

    public List<TrainStopDTO> findAll() {
        final List<TrainStop> trainStops = trainStopRepository.findAll(Sort.by("seq"));
        return trainStops.stream()
                .map(trainStop -> mapToDTO(trainStop, new TrainStopDTO()))
                .toList();
    }

    public TrainStopDTO get(final String seq) {
        return trainStopRepository.findById(seq)
                .map(trainStop -> mapToDTO(trainStop, new TrainStopDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TrainStopDTO trainStopDTO) {
        final TrainStop trainStop = new TrainStop();
        mapToEntity(trainStopDTO, trainStop);
        trainStop.setSeq(trainStopDTO.getSeq());
        return trainStopRepository.save(trainStop).getSeq();
    }

    public void update(final String seq, final TrainStopDTO trainStopDTO) {
        final TrainStop trainStop = trainStopRepository.findById(seq)
                .orElseThrow(NotFoundException::new);
        mapToEntity(trainStopDTO, trainStop);
        trainStopRepository.save(trainStop);
    }

    public void delete(final String seq) {
        final TrainStop trainStop = trainStopRepository.findById(seq)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTrainStop(seq));
        trainStopRepository.delete(trainStop);
    }

    private TrainStopDTO mapToDTO(final TrainStop trainStop, final TrainStopDTO trainStopDTO) {
        trainStopDTO.setSeq(trainStop.getSeq());
        trainStopDTO.setArrivesAt(trainStop.getArrivesAt());
        trainStopDTO.setDepartsAt(trainStop.getDepartsAt());
        trainStopDTO.setDayOffset(trainStop.getDayOffset());
        trainStopDTO.setDistanceKm(trainStop.getDistanceKm());
        trainStopDTO.setTrainNumber(trainStop.getTrainNumber() == null ? null : trainStop.getTrainNumber().getNumber());
        trainStopDTO.setStationCode(trainStop.getStationCode() == null ? null : trainStop.getStationCode().getCode());
        return trainStopDTO;
    }

    private TrainStop mapToEntity(final TrainStopDTO trainStopDTO, final TrainStop trainStop) {
        trainStop.setArrivesAt(trainStopDTO.getArrivesAt());
        trainStop.setDepartsAt(trainStopDTO.getDepartsAt());
        trainStop.setDayOffset(trainStopDTO.getDayOffset());
        trainStop.setDistanceKm(trainStopDTO.getDistanceKm());
        final Train trainNumber = trainStopDTO.getTrainNumber() == null ? null : trainRepository.findById(trainStopDTO.getTrainNumber())
                .orElseThrow(() -> new NotFoundException("trainNumber not found"));
        trainStop.setTrainNumber(trainNumber);
        final Station stationCode = trainStopDTO.getStationCode() == null ? null : stationRepository.findById(trainStopDTO.getStationCode())
                .orElseThrow(() -> new NotFoundException("stationCode not found"));
        trainStop.setStationCode(stationCode);
        return trainStop;
    }

    public boolean seqExists(final String seq) {
        return trainStopRepository.existsBySeqIgnoreCase(seq);
    }

    @EventListener(BeforeDeleteTrain.class)
    public void on(final BeforeDeleteTrain event) {
        final ReferencedException referencedException = new ReferencedException();
        final TrainStop trainNumberTrainStop = trainStopRepository.findFirstByTrainNumberNumber(event.getNumber());
        if (trainNumberTrainStop != null) {
            referencedException.setKey("train.trainStop.trainNumber.referenced");
            referencedException.addParam(trainNumberTrainStop.getSeq());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStation.class)
    public void on(final BeforeDeleteStation event) {
        final ReferencedException referencedException = new ReferencedException();
        final TrainStop stationCodeTrainStop = trainStopRepository.findFirstByStationCodeCode(event.getCode());
        if (stationCodeTrainStop != null) {
            referencedException.setKey("station.trainStop.stationCode.referenced");
            referencedException.addParam(stationCodeTrainStop.getSeq());
            throw referencedException;
        }
    }

}
