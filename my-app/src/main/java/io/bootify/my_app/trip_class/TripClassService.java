package io.bootify.my_app.trip_class;

import io.bootify.my_app.events.BeforeDeleteTravelClass;
import io.bootify.my_app.events.BeforeDeleteTrip;
import io.bootify.my_app.events.BeforeDeleteTripClass;
import io.bootify.my_app.travel_class.TravelClass;
import io.bootify.my_app.travel_class.TravelClassRepository;
import io.bootify.my_app.trip.Trip;
import io.bootify.my_app.trip.TripRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TripClassService {

    private final TripClassRepository tripClassRepository;
    private final TripRepository tripRepository;
    private final TravelClassRepository travelClassRepository;
    private final ApplicationEventPublisher publisher;

    public TripClassService(final TripClassRepository tripClassRepository,
            final TripRepository tripRepository, final TravelClassRepository travelClassRepository,
            final ApplicationEventPublisher publisher) {
        this.tripClassRepository = tripClassRepository;
        this.tripRepository = tripRepository;
        this.travelClassRepository = travelClassRepository;
        this.publisher = publisher;
    }

    public List<TripClassDTO> findAll() {
        final List<TripClass> tripClasses = tripClassRepository.findAll(Sort.by("id"));
        return tripClasses.stream()
                .map(tripClass -> mapToDTO(tripClass, new TripClassDTO()))
                .toList();
    }

    public TripClassDTO get(final Long id) {
        return tripClassRepository.findById(id)
                .map(tripClass -> mapToDTO(tripClass, new TripClassDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TripClassDTO tripClassDTO) {
        final TripClass tripClass = new TripClass();
        mapToEntity(tripClassDTO, tripClass);
        return tripClassRepository.save(tripClass).getId();
    }

    public void update(final Long id, final TripClassDTO tripClassDTO) {
        final TripClass tripClass = tripClassRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tripClassDTO, tripClass);
        tripClassRepository.save(tripClass);
    }

    public void delete(final Long id) {
        final TripClass tripClass = tripClassRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTripClass(id));
        tripClassRepository.delete(tripClass);
    }

    private TripClassDTO mapToDTO(final TripClass tripClass, final TripClassDTO tripClassDTO) {
        tripClassDTO.setId(tripClass.getId());
        tripClassDTO.setTatkalOpensAt(tripClass.getTatkalOpensAt());
        tripClassDTO.setTrip(tripClass.getTrip() == null ? null : tripClass.getTrip().getId());
        tripClassDTO.setClassCode(tripClass.getClassCode() == null ? null : tripClass.getClassCode().getCode());
        return tripClassDTO;
    }

    private TripClass mapToEntity(final TripClassDTO tripClassDTO, final TripClass tripClass) {
        tripClass.setTatkalOpensAt(tripClassDTO.getTatkalOpensAt());
        final Trip trip = tripClassDTO.getTrip() == null ? null : tripRepository.findById(tripClassDTO.getTrip())
                .orElseThrow(() -> new NotFoundException("trip not found"));
        tripClass.setTrip(trip);
        final TravelClass classCode = tripClassDTO.getClassCode() == null ? null : travelClassRepository.findById(tripClassDTO.getClassCode())
                .orElseThrow(() -> new NotFoundException("classCode not found"));
        tripClass.setClassCode(classCode);
        return tripClass;
    }

    @EventListener(BeforeDeleteTrip.class)
    public void on(final BeforeDeleteTrip event) {
        final ReferencedException referencedException = new ReferencedException();
        final TripClass tripTripClass = tripClassRepository.findFirstByTripId(event.getId());
        if (tripTripClass != null) {
            referencedException.setKey("trip.tripClass.trip.referenced");
            referencedException.addParam(tripTripClass.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTravelClass.class)
    public void on(final BeforeDeleteTravelClass event) {
        final ReferencedException referencedException = new ReferencedException();
        final TripClass classCodeTripClass = tripClassRepository.findFirstByClassCodeCode(event.getCode());
        if (classCodeTripClass != null) {
            referencedException.setKey("travelClass.tripClass.classCode.referenced");
            referencedException.addParam(classCodeTripClass.getId());
            throw referencedException;
        }
    }

}
