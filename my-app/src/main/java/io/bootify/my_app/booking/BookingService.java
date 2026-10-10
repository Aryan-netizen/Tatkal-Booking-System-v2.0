package io.bootify.my_app.booking;

import io.bootify.my_app.events.BeforeDeleteBooking;
import io.bootify.my_app.events.BeforeDeleteQuota;
import io.bootify.my_app.events.BeforeDeleteTrainStop;
import io.bootify.my_app.events.BeforeDeleteTrip;
import io.bootify.my_app.quota.Quota;
import io.bootify.my_app.quota.QuotaRepository;
import io.bootify.my_app.train_stop.TrainStop;
import io.bootify.my_app.train_stop.TrainStopRepository;
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
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final TrainStopRepository trainStopRepository;
    private final QuotaRepository quotaRepository;
    private final ApplicationEventPublisher publisher;

    public BookingService(final BookingRepository bookingRepository,
            final TripRepository tripRepository, final TrainStopRepository trainStopRepository,
            final QuotaRepository quotaRepository, final ApplicationEventPublisher publisher) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.trainStopRepository = trainStopRepository;
        this.quotaRepository = quotaRepository;
        this.publisher = publisher;
    }

    public List<BookingDTO> findAll() {
        final List<Booking> bookings = bookingRepository.findAll(Sort.by("id"));
        return bookings.stream()
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .toList();
    }

    public BookingDTO get(final String id) {
        return bookingRepository.findById(id)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        booking.setId(bookingDTO.getId());
        return bookingRepository.save(booking).getId();
    }

    public void update(final String id, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final String id) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBooking(id));
        bookingRepository.delete(booking);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setId(booking.getId());
        bookingDTO.setPnr(booking.getPnr());
        bookingDTO.setUserId(booking.getUserId());
        bookingDTO.setIdempotencyKey(booking.getIdempotencyKey());
        bookingDTO.setFromSeq(booking.getFromSeq());
        bookingDTO.setToSeq(booking.getToSeq());
        bookingDTO.setClassCode(booking.getClassCode());
        bookingDTO.setSeatCount(booking.getSeatCount());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setFarePaise(booking.getFarePaise());
        bookingDTO.setTatkalChargePaise(booking.getTatkalChargePaise());
        bookingDTO.setOtherChargesPaise(booking.getOtherChargesPaise());
        bookingDTO.setGstPaise(booking.getGstPaise());
        bookingDTO.setAmountPaise(booking.getAmountPaise());
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        bookingDTO.setConfirmedAt(booking.getConfirmedAt());
        bookingDTO.setUpdatedAt(booking.getUpdatedAt());
        bookingDTO.setTrip(booking.getTrip() == null ? null : booking.getTrip().getId());
        bookingDTO.setTrainNumber(booking.getTrainNumber() == null ? null : booking.getTrainNumber().getSeq());
        bookingDTO.setQuota(booking.getQuota() == null ? null : booking.getQuota().getCode());
        return bookingDTO;
    }

    private Booking mapToEntity(final BookingDTO bookingDTO, final Booking booking) {
        booking.setPnr(bookingDTO.getPnr());
        booking.setUserId(bookingDTO.getUserId());
        booking.setIdempotencyKey(bookingDTO.getIdempotencyKey());
        booking.setFromSeq(bookingDTO.getFromSeq());
        booking.setToSeq(bookingDTO.getToSeq());
        booking.setClassCode(bookingDTO.getClassCode());
        booking.setSeatCount(bookingDTO.getSeatCount());
        booking.setStatus(bookingDTO.getStatus());
        booking.setFarePaise(bookingDTO.getFarePaise());
        booking.setTatkalChargePaise(bookingDTO.getTatkalChargePaise());
        booking.setOtherChargesPaise(bookingDTO.getOtherChargesPaise());
        booking.setGstPaise(bookingDTO.getGstPaise());
        booking.setAmountPaise(bookingDTO.getAmountPaise());
        booking.setCreatedAt(bookingDTO.getCreatedAt());
        booking.setConfirmedAt(bookingDTO.getConfirmedAt());
        booking.setUpdatedAt(bookingDTO.getUpdatedAt());
        final Trip trip = bookingDTO.getTrip() == null ? null : tripRepository.findById(bookingDTO.getTrip())
                .orElseThrow(() -> new NotFoundException("trip not found"));
        booking.setTrip(trip);
        final TrainStop trainNumber = bookingDTO.getTrainNumber() == null ? null : trainStopRepository.findById(bookingDTO.getTrainNumber())
                .orElseThrow(() -> new NotFoundException("trainNumber not found"));
        booking.setTrainNumber(trainNumber);
        final Quota quota = bookingDTO.getQuota() == null ? null : quotaRepository.findById(bookingDTO.getQuota())
                .orElseThrow(() -> new NotFoundException("quota not found"));
        booking.setQuota(quota);
        return booking;
    }

    public boolean idExists(final String id) {
        return bookingRepository.existsByIdIgnoreCase(id);
    }

    public boolean pnrExists(final String pnr) {
        return bookingRepository.existsByPnrIgnoreCase(pnr);
    }

    @EventListener(BeforeDeleteTrip.class)
    public void on(final BeforeDeleteTrip event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking tripBooking = bookingRepository.findFirstByTripId(event.getId());
        if (tripBooking != null) {
            referencedException.setKey("trip.booking.trip.referenced");
            referencedException.addParam(tripBooking.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTrainStop.class)
    public void on(final BeforeDeleteTrainStop event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking trainNumberBooking = bookingRepository.findFirstByTrainNumberSeq(event.getSeq());
        if (trainNumberBooking != null) {
            referencedException.setKey("trainStop.booking.trainNumber.referenced");
            referencedException.addParam(trainNumberBooking.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteQuota.class)
    public void on(final BeforeDeleteQuota event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking quotaBooking = bookingRepository.findFirstByQuotaCode(event.getCode());
        if (quotaBooking != null) {
            referencedException.setKey("quota.booking.quota.referenced");
            referencedException.addParam(quotaBooking.getId());
            throw referencedException;
        }
    }

}
