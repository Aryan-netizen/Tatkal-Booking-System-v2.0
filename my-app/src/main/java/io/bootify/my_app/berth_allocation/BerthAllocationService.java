package io.bootify.my_app.berth_allocation;

import io.bootify.my_app.berth.Berth;
import io.bootify.my_app.berth.BerthRepository;
import io.bootify.my_app.events.BeforeDeleteBerth;
import io.bootify.my_app.events.BeforeDeleteBerthAllocation;
import io.bootify.my_app.events.BeforeDeletePassenger;
import io.bootify.my_app.events.BeforeDeleteTrip;
import io.bootify.my_app.passenger.Passenger;
import io.bootify.my_app.passenger.PassengerRepository;
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
public class BerthAllocationService {

    private final BerthAllocationRepository berthAllocationRepository;
    private final TripRepository tripRepository;
    private final BerthRepository berthRepository;
    private final PassengerRepository passengerRepository;
    private final ApplicationEventPublisher publisher;

    public BerthAllocationService(final BerthAllocationRepository berthAllocationRepository,
            final TripRepository tripRepository, final BerthRepository berthRepository,
            final PassengerRepository passengerRepository,
            final ApplicationEventPublisher publisher) {
        this.berthAllocationRepository = berthAllocationRepository;
        this.tripRepository = tripRepository;
        this.berthRepository = berthRepository;
        this.passengerRepository = passengerRepository;
        this.publisher = publisher;
    }

    public List<BerthAllocationDTO> findAll() {
        final List<BerthAllocation> berthAllocations = berthAllocationRepository.findAll(Sort.by("id"));
        return berthAllocations.stream()
                .map(berthAllocation -> mapToDTO(berthAllocation, new BerthAllocationDTO()))
                .toList();
    }

    public BerthAllocationDTO get(final String id) {
        return berthAllocationRepository.findById(id)
                .map(berthAllocation -> mapToDTO(berthAllocation, new BerthAllocationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final BerthAllocationDTO berthAllocationDTO) {
        final BerthAllocation berthAllocation = new BerthAllocation();
        mapToEntity(berthAllocationDTO, berthAllocation);
        berthAllocation.setId(berthAllocationDTO.getId());
        return berthAllocationRepository.save(berthAllocation).getId();
    }

    public void update(final String id, final BerthAllocationDTO berthAllocationDTO) {
        final BerthAllocation berthAllocation = berthAllocationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(berthAllocationDTO, berthAllocation);
        berthAllocationRepository.save(berthAllocation);
    }

    public void delete(final String id) {
        final BerthAllocation berthAllocation = berthAllocationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBerthAllocation(id));
        berthAllocationRepository.delete(berthAllocation);
    }

    private BerthAllocationDTO mapToDTO(final BerthAllocation berthAllocation,
            final BerthAllocationDTO berthAllocationDTO) {
        berthAllocationDTO.setId(berthAllocation.getId());
        berthAllocationDTO.setFromSeq(berthAllocation.getFromSeq());
        berthAllocationDTO.setToSeq(berthAllocation.getToSeq());
        berthAllocationDTO.setTrip(berthAllocation.getTrip() == null ? null : berthAllocation.getTrip().getId());
        berthAllocationDTO.setBerth(berthAllocation.getBerth() == null ? null : berthAllocation.getBerth().getId());
        berthAllocationDTO.setPassenger(berthAllocation.getPassenger() == null ? null : berthAllocation.getPassenger().getId());
        return berthAllocationDTO;
    }

    private BerthAllocation mapToEntity(final BerthAllocationDTO berthAllocationDTO,
            final BerthAllocation berthAllocation) {
        berthAllocation.setFromSeq(berthAllocationDTO.getFromSeq());
        berthAllocation.setToSeq(berthAllocationDTO.getToSeq());
        final Trip trip = berthAllocationDTO.getTrip() == null ? null : tripRepository.findById(berthAllocationDTO.getTrip())
                .orElseThrow(() -> new NotFoundException("trip not found"));
        berthAllocation.setTrip(trip);
        final Berth berth = berthAllocationDTO.getBerth() == null ? null : berthRepository.findById(berthAllocationDTO.getBerth())
                .orElseThrow(() -> new NotFoundException("berth not found"));
        berthAllocation.setBerth(berth);
        final Passenger passenger = berthAllocationDTO.getPassenger() == null ? null : passengerRepository.findById(berthAllocationDTO.getPassenger())
                .orElseThrow(() -> new NotFoundException("passenger not found"));
        berthAllocation.setPassenger(passenger);
        return berthAllocation;
    }

    public boolean idExists(final String id) {
        return berthAllocationRepository.existsByIdIgnoreCase(id);
    }

    @EventListener(BeforeDeleteTrip.class)
    public void on(final BeforeDeleteTrip event) {
        final ReferencedException referencedException = new ReferencedException();
        final BerthAllocation tripBerthAllocation = berthAllocationRepository.findFirstByTripId(event.getId());
        if (tripBerthAllocation != null) {
            referencedException.setKey("trip.berthAllocation.trip.referenced");
            referencedException.addParam(tripBerthAllocation.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteBerth.class)
    public void on(final BeforeDeleteBerth event) {
        final ReferencedException referencedException = new ReferencedException();
        final BerthAllocation berthBerthAllocation = berthAllocationRepository.findFirstByBerthId(event.getId());
        if (berthBerthAllocation != null) {
            referencedException.setKey("berth.berthAllocation.berth.referenced");
            referencedException.addParam(berthBerthAllocation.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeletePassenger.class)
    public void on(final BeforeDeletePassenger event) {
        final ReferencedException referencedException = new ReferencedException();
        final BerthAllocation passengerBerthAllocation = berthAllocationRepository.findFirstByPassengerId(event.getId());
        if (passengerBerthAllocation != null) {
            referencedException.setKey("passenger.berthAllocation.passenger.referenced");
            referencedException.addParam(passengerBerthAllocation.getId());
            throw referencedException;
        }
    }

}
