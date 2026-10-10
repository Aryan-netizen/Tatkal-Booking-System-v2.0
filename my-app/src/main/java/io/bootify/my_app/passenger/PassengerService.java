package io.bootify.my_app.passenger;

import io.bootify.my_app.booking.Booking;
import io.bootify.my_app.booking.BookingRepository;
import io.bootify.my_app.events.BeforeDeleteBooking;
import io.bootify.my_app.events.BeforeDeletePassenger;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher publisher;

    public PassengerService(final PassengerRepository passengerRepository,
            final BookingRepository bookingRepository, final ApplicationEventPublisher publisher) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.publisher = publisher;
    }

    public List<PassengerDTO> findAll() {
        final List<Passenger> passengers = passengerRepository.findAll(Sort.by("id"));
        return passengers.stream()
                .map(passenger -> mapToDTO(passenger, new PassengerDTO()))
                .toList();
    }

    public PassengerDTO get(final String id) {
        return passengerRepository.findById(id)
                .map(passenger -> mapToDTO(passenger, new PassengerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final PassengerDTO passengerDTO) {
        final Passenger passenger = new Passenger();
        mapToEntity(passengerDTO, passenger);
        passenger.setId(passengerDTO.getId());
        return passengerRepository.save(passenger).getId();
    }

    public void update(final String id, final PassengerDTO passengerDTO) {
        final Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(passengerDTO, passenger);
        passengerRepository.save(passenger);
    }

    public void delete(final String id) {
        final Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeletePassenger(id));
        passengerRepository.delete(passenger);
    }

    private PassengerDTO mapToDTO(final Passenger passenger, final PassengerDTO passengerDTO) {
        passengerDTO.setId(passenger.getId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setAge(passenger.getAge());
        passengerDTO.setGender(passenger.getGender());
        passengerDTO.setBerthPref(passenger.getBerthPref());
        passengerDTO.setStatus(passenger.getStatus());
        passengerDTO.setQueueNo(passenger.getQueueNo());
        passengerDTO.setBooking(passenger.getBooking() == null ? null : passenger.getBooking().getId());
        return passengerDTO;
    }

    private Passenger mapToEntity(final PassengerDTO passengerDTO, final Passenger passenger) {
        passenger.setName(passengerDTO.getName());
        passenger.setAge(passengerDTO.getAge());
        passenger.setGender(passengerDTO.getGender());
        passenger.setBerthPref(passengerDTO.getBerthPref());
        passenger.setStatus(passengerDTO.getStatus());
        passenger.setQueueNo(passengerDTO.getQueueNo());
        final Booking booking = passengerDTO.getBooking() == null ? null : bookingRepository.findById(passengerDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        passenger.setBooking(booking);
        return passenger;
    }

    public boolean idExists(final String id) {
        return passengerRepository.existsByIdIgnoreCase(id);
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final Passenger bookingPassenger = passengerRepository.findFirstByBookingId(event.getId());
        if (bookingPassenger != null) {
            referencedException.setKey("booking.passenger.booking.referenced");
            referencedException.addParam(bookingPassenger.getId());
            throw referencedException;
        }
    }

}
