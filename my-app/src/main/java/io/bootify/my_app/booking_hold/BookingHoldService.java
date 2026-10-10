package io.bootify.my_app.booking_hold;

import io.bootify.my_app.booking.Booking;
import io.bootify.my_app.booking.BookingRepository;
import io.bootify.my_app.events.BeforeDeleteBooking;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookingHoldService {

    private final BookingHoldRepository bookingHoldRepository;
    private final BookingRepository bookingRepository;

    public BookingHoldService(final BookingHoldRepository bookingHoldRepository,
            final BookingRepository bookingRepository) {
        this.bookingHoldRepository = bookingHoldRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<BookingHoldDTO> findAll() {
        final List<BookingHold> bookingHolds = bookingHoldRepository.findAll(Sort.by("id"));
        return bookingHolds.stream()
                .map(bookingHold -> mapToDTO(bookingHold, new BookingHoldDTO()))
                .toList();
    }

    public BookingHoldDTO get(final Long id) {
        return bookingHoldRepository.findById(id)
                .map(bookingHold -> mapToDTO(bookingHold, new BookingHoldDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookingHoldDTO bookingHoldDTO) {
        final BookingHold bookingHold = new BookingHold();
        mapToEntity(bookingHoldDTO, bookingHold);
        return bookingHoldRepository.save(bookingHold).getId();
    }

    public void update(final Long id, final BookingHoldDTO bookingHoldDTO) {
        final BookingHold bookingHold = bookingHoldRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingHoldDTO, bookingHold);
        bookingHoldRepository.save(bookingHold);
    }

    public void delete(final Long id) {
        final BookingHold bookingHold = bookingHoldRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        bookingHoldRepository.delete(bookingHold);
    }

    private BookingHoldDTO mapToDTO(final BookingHold bookingHold,
            final BookingHoldDTO bookingHoldDTO) {
        bookingHoldDTO.setId(bookingHold.getId());
        bookingHoldDTO.setHoldExpiresAt(bookingHold.getHoldExpiresAt());
        bookingHoldDTO.setBooking(bookingHold.getBooking() == null ? null : bookingHold.getBooking().getId());
        return bookingHoldDTO;
    }

    private BookingHold mapToEntity(final BookingHoldDTO bookingHoldDTO,
            final BookingHold bookingHold) {
        bookingHold.setHoldExpiresAt(bookingHoldDTO.getHoldExpiresAt());
        final Booking booking = bookingHoldDTO.getBooking() == null ? null : bookingRepository.findById(bookingHoldDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        bookingHold.setBooking(booking);
        return bookingHold;
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final BookingHold bookingBookingHold = bookingHoldRepository.findFirstByBookingId(event.getId());
        if (bookingBookingHold != null) {
            referencedException.setKey("booking.bookingHold.booking.referenced");
            referencedException.addParam(bookingBookingHold.getId());
            throw referencedException;
        }
    }

}
