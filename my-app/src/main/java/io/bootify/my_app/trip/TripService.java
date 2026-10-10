package io.bootify.my_app.trip;

import io.bootify.my_app.events.BeforeDeleteTrain;
import io.bootify.my_app.events.BeforeDeleteTrip;
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
public class TripService {

    private final TripRepository tripRepository;
    private final TrainRepository trainRepository;
    private final ApplicationEventPublisher publisher;

    public TripService(final TripRepository tripRepository, final TrainRepository trainRepository,
            final ApplicationEventPublisher publisher) {
        this.tripRepository = tripRepository;
        this.trainRepository = trainRepository;
        this.publisher = publisher;
    }

    public List<TripDTO> findAll() {
        final List<Trip> trips = tripRepository.findAll(Sort.by("id"));
        return trips.stream()
                .map(trip -> mapToDTO(trip, new TripDTO()))
                .toList();
    }

    public TripDTO get(final String id) {
        return tripRepository.findById(id)
                .map(trip -> mapToDTO(trip, new TripDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TripDTO tripDTO) {
        final Trip trip = new Trip();
        mapToEntity(tripDTO, trip);
        trip.setId(tripDTO.getId());
        return tripRepository.save(trip).getId();
    }

    public void update(final String id, final TripDTO tripDTO) {
        final Trip trip = tripRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tripDTO, trip);
        tripRepository.save(trip);
    }

    public void delete(final String id) {
        final Trip trip = tripRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTrip(id));
        tripRepository.delete(trip);
    }

    private TripDTO mapToDTO(final Trip trip, final TripDTO tripDTO) {
        tripDTO.setId(trip.getId());
        tripDTO.setTravelDate(trip.getTravelDate());
        tripDTO.setStatus(trip.getStatus());
        tripDTO.setTrainNumber(trip.getTrainNumber() == null ? null : trip.getTrainNumber().getNumber());
        return tripDTO;
    }

    private Trip mapToEntity(final TripDTO tripDTO, final Trip trip) {
        trip.setTravelDate(tripDTO.getTravelDate());
        trip.setStatus(tripDTO.getStatus());
        final Train trainNumber = tripDTO.getTrainNumber() == null ? null : trainRepository.findById(tripDTO.getTrainNumber())
                .orElseThrow(() -> new NotFoundException("trainNumber not found"));
        trip.setTrainNumber(trainNumber);
        return trip;
    }

    public boolean idExists(final String id) {
        return tripRepository.existsByIdIgnoreCase(id);
    }

    @EventListener(BeforeDeleteTrain.class)
    public void on(final BeforeDeleteTrain event) {
        final ReferencedException referencedException = new ReferencedException();
        final Trip trainNumberTrip = tripRepository.findFirstByTrainNumberNumber(event.getNumber());
        if (trainNumberTrip != null) {
            referencedException.setKey("train.trip.trainNumber.referenced");
            referencedException.addParam(trainNumberTrip.getId());
            throw referencedException;
        }
    }

}
